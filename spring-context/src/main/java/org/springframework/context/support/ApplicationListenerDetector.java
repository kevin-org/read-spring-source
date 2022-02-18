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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 检测实现{@code ApplicationListener}接口的bean。
 * 这会捕获不能被{@code getBeanNamesForType}可靠地检测到的bean 和 只对顶级bean有效的相关操作。
 *
 * <p>在标准Java序列化中，这个后处理器不会作为{@code DisposableBeanAdapter}的一部分进行序列化。
 * 但是，使用可选的序列化机制，{@code DisposableBeanAdapter。writeReplace}可能根本不会被使用
 * ，所以我们防御地将这个后处理器的字段状态标记为{@code transient}。
 *
 * @author Juergen Hoeller
 * @since 4.3.4
 */
class ApplicationListenerDetector implements DestructionAwareBeanPostProcessor, MergedBeanDefinitionPostProcessor {

	private static final Log logger = LogFactory.getLog(ApplicationListenerDetector.class);

	private transient final AbstractApplicationContext applicationContext;

	private transient final Map<String, Boolean> singletonNames = new ConcurrentHashMap<String, Boolean>(256);


	public ApplicationListenerDetector(AbstractApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}


	@Override
	public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
		if (this.applicationContext != null) {
			this.singletonNames.put(beanName, beanDefinition.isSingleton());
		}
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) {
		ConsoleOutputUtils.hr("ApplicationListenerDetector - postProcessBeforeInitialization");
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) {
		ConsoleOutputUtils.hr("ApplicationListenerDetector - postProcessAfterInitialization");
		if (this.applicationContext != null && bean instanceof ApplicationListener) {
			// potentially not detected as a listener by getBeanNamesForType retrieval
			Boolean flag = this.singletonNames.get(beanName);
			if (Boolean.TRUE.equals(flag)) {
				// singleton bean (top-level or inner): register on the fly
				this.applicationContext.addApplicationListener((ApplicationListener<?>) bean);
			}
			else if (Boolean.FALSE.equals(flag)) {
				if (logger.isWarnEnabled() && !this.applicationContext.containsBean(beanName)) {
					// inner bean with other scope - can't reliably process events
					logger.warn("Inner bean '" + beanName + "' implements ApplicationListener interface " +
							"but is not reachable for event multicasting by its containing ApplicationContext " +
							"because it does not have singleton scope. Only top-level listener beans are allowed " +
							"to be of non-singleton scope.");
				}
				this.singletonNames.remove(beanName);
			}
		}
		return bean;
	}

	@Override
	public void postProcessBeforeDestruction(Object bean, String beanName) {
		if (this.applicationContext != null && bean instanceof ApplicationListener) {
			ApplicationEventMulticaster multicaster = this.applicationContext.getApplicationEventMulticaster();
			multicaster.removeApplicationListener((ApplicationListener<?>) bean);
			multicaster.removeApplicationListenerBean(beanName);
		}
	}

	@Override
	public boolean requiresDestruction(Object bean) {
		return (bean instanceof ApplicationListener);
	}


	@Override
	public boolean equals(Object other) {
		return (this == other || (other instanceof ApplicationListenerDetector &&
				this.applicationContext == ((ApplicationListenerDetector) other).applicationContext));
	}

	@Override
	public int hashCode() {
		return ObjectUtils.nullSafeHashCode(this.applicationContext);
	}

}
