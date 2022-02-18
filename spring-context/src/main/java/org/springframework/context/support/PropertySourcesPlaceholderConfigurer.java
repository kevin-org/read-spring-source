/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.context.support;

import com.kevin.common.utils.print.ConsoleOutputUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurablePropertyResolver;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.util.Assert;
import org.springframework.util.StringValueResolver;

import java.io.IOException;
import java.util.Properties;

/**
 * 解析${…的{@link PlaceholderConfigurerSupport}的专门化针对
 * 当前Spring {@link Environment}和它的{@link PropertySources}集，bean定义属性值和{@code @Value}注释中的占位符。
 *
 * <p>这个类被设计为Spring 3.1应用程序中{@code PropertyPlaceholderConfigurer}的常规替代。
 * 它默认用于支持{@code属性-占位符}在spring-context-3.1 XSD上工作，而spring-context版本
 * &lt;= 3.0默认为{@code PropertyPlaceholderConfigurer}以确保向后兼容。有关完整细节，请参阅spring上下文XSD文档。
 *
 * <p>任何本地属性(例如那些通过{@link #setProperties}， {@link #setLocations}等添加的属性)都被添加为{@code PropertySource}。
 * 本地属性的搜索优先级基于{@link #setLocalOverride localOverride}属性的值，该属性在默认情况下为{@code false}
 * ，表示本地属性将在所有环境属性源之后最后搜索。
 *
 * <p>See {@link org.springframework.core.env.ConfigurableEnvironment ConfigurableEnvironment}
 * and related javadocs for details on manipulating environment property sources.
 *
 * @author Chris Beams
 * @since 3.1
 * @see org.springframework.core.env.ConfigurableEnvironment
 * @see PlaceholderConfigurerSupport
 * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
 */
public class PropertySourcesPlaceholderConfigurer extends PlaceholderConfigurerSupport implements EnvironmentAware {

	/**
	 * {@value} is the name given to the {@link PropertySource} for the set of
	 * {@linkplain #mergeProperties() merged properties} supplied to this configurer.
	 */
	public static final String LOCAL_PROPERTIES_PROPERTY_SOURCE_NAME = "localProperties";

	/**
	 * {@value} is the name given to the {@link PropertySource} that wraps the
	 * {@linkplain #setEnvironment environment} supplied to this configurer.
	 */
	public static final String ENVIRONMENT_PROPERTIES_PROPERTY_SOURCE_NAME = "environmentProperties";


	private MutablePropertySources propertySources;

	private PropertySources appliedPropertySources;

	private Environment environment;


	/**
	 * Customize the set of {@link PropertySources} to be used by this configurer.
	 * Setting this property indicates that environment property sources and local
	 * properties should be ignored.
	 * @see #postProcessBeanFactory
	 */
	public void setPropertySources(PropertySources propertySources) {
		this.propertySources = new MutablePropertySources(propertySources);
	}

	/**
	 * {@inheritDoc}
	 * <p>{@code PropertySources} from this environment will be searched when replacing ${...} placeholders.
	 * @see #setPropertySources
	 * @see #postProcessBeanFactory
	 */
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}


	/**
	 * {@inheritDoc}
	 * <p>处理通过替换${…通过根据配置器的{@link PropertySources}集解析每个bean定义中的占位符，其中包括:
	 * <ul>
	 * <li>all {@linkplain org.springframework.core.env.ConfigurableEnvironment#getPropertySources
	 * environment property sources}, if an {@code Environment} {@linkplain #setEnvironment is present}
	 * <li>{@linkplain #mergeProperties merged local properties}, if {@linkplain #setLocation any}
	 * {@linkplain #setLocations have} {@linkplain #setProperties been}
	 * {@linkplain #setPropertiesArray specified}
	 * <li>any property sources set by calling {@link #setPropertySources}
	 * </ul>
	 * <p>如果调用{@link #setPropertySources}， <strong>环境和本地属性将被忽略</strong>。
	 * 此方法旨在让用户对属性源进行细粒度控制，一旦设置好，配置程序就不会假设要添加其他源。
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		ConsoleOutputUtils.hrl("PropertySourcesPlaceholderConfigurer - postProcessBeanFactory");

		if (this.propertySources == null) {
			this.propertySources = new MutablePropertySources();
			if (this.environment != null) {
				this.propertySources.addLast(
					new PropertySource<Environment>(ENVIRONMENT_PROPERTIES_PROPERTY_SOURCE_NAME, this.environment) {
						@Override
						public String getProperty(String key) {
							return this.source.getProperty(key);
						}
					}
				);
			}
			try {
				PropertySource<?> localPropertySource =
						new PropertiesPropertySource(LOCAL_PROPERTIES_PROPERTY_SOURCE_NAME, mergeProperties());
				if (this.localOverride) {
					this.propertySources.addFirst(localPropertySource);
				}
				else {
					this.propertySources.addLast(localPropertySource);
				}
			}
			catch (IOException ex) {
				throw new BeanInitializationException("Could not load properties", ex);
			}
		}
		//${}占位符替换为真实值
		processProperties(beanFactory, new PropertySourcesPropertyResolver(this.propertySources));
		this.appliedPropertySources = this.propertySources;
	}

	/**
	 * 访问给定bean工厂中的每个bean定义，并尝试替换${…具有给定属性值的属性占位符。
	 */
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,
			final ConfigurablePropertyResolver propertyResolver) throws BeansException {

		propertyResolver.setPlaceholderPrefix(this.placeholderPrefix);
		propertyResolver.setPlaceholderSuffix(this.placeholderSuffix);
		propertyResolver.setValueSeparator(this.valueSeparator);

		StringValueResolver valueResolver = new StringValueResolver() {
			@Override
			public String resolveStringValue(String strVal) {
				String resolved = (ignoreUnresolvablePlaceholders ?
						propertyResolver.resolvePlaceholders(strVal) :
						propertyResolver.resolveRequiredPlaceholders(strVal));
				if (trimValues) {
					resolved = resolved.trim();
				}
				return (resolved.equals(nullValue) ? null : resolved);
			}
		};

		doProcessProperties(beanFactoryToProcess, valueResolver);
	}

	/**
	 * Implemented for compatibility with {@link PlaceholderConfigurerSupport}.
	 * @deprecated in favor of {@link #processProperties(ConfigurableListableBeanFactory, ConfigurablePropertyResolver)}
	 * @throws UnsupportedOperationException
	 */
	@Override
	@Deprecated
	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) {
		throw new UnsupportedOperationException(
				"Call processProperties(ConfigurableListableBeanFactory, ConfigurablePropertyResolver) instead");
	}

	/**
	 * Returns the property sources that were actually applied during
	 * {@link #postProcessBeanFactory(ConfigurableListableBeanFactory) post-processing}.
	 * @return the property sources that were applied
	 * @throws IllegalStateException if the property sources have not yet been applied
	 * @since 4.0
	 */
	public PropertySources getAppliedPropertySources() throws IllegalStateException {
		Assert.state(this.appliedPropertySources != null, "PropertySources have not get been applied");
		return this.appliedPropertySources;
	}

}
