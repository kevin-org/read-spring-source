/*
 * Copyright 2002-2013 the original author or authors.
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

package org.springframework.context.annotation;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 当处理@{@link Configuration}类时，要由注册额外bean定义的类型实现的接口。
 * 在bean定义级别(相对于{@code @Bean}方法/实例级别)操作时，是需要或必需的。
 *
 * <p>与{@code @ Configuration}和{@link ImportSelector}一起
 * ，这种类型的类可以提供给@{@link Import}注释(或者也可以从{@code ImportSelector}返回)。
 *
 * <p>一个{@link ImportBeanDefinitionRegistrar}可以实现以下任何一个
 * {@link org.springframework.beans.factory。接口和它们各自的方法将在{@link #registerBeanDefinitions}之前被调用:
 * 意思是，调用import的方法之前，会先通过钩子方法注入依赖 容器Bean
 * <ul>
 * <li>{@link org.springframework.context.EnvironmentAware EnvironmentAware}</li>
 * <li>{@link org.springframework.beans.factory.BeanFactoryAware BeanFactoryAware}
 * <li>{@link org.springframework.beans.factory.BeanClassLoaderAware BeanClassLoaderAware}
 * <li>{@link org.springframework.context.ResourceLoaderAware ResourceLoaderAware}
 * </ul>
 *
 * <p>有关使用示例，请参见实现和相关的单元测试。
 *
 * @author Chris Beams
 * @since 3.1
 * @see Import
 * @see ImportSelector
 * @see Configuration
 */
public interface ImportBeanDefinitionRegistrar {

	/**
	 * 根据导入的{@code @Configuration}类的给定注释元数据，必要时注册bean定义。
	 * <p>注意，{@link BeanDefinitionRegistryPostProcessor}类型可能<em>不</em>在这里被注册
	 * ，这是由于与{@code @Configuration}类处理相关的生命周期约束。
	 *
	 * @param importingClassMetadata annotation metadata of the importing class
	 * @param registry current bean definition registry
	 */
	public void registerBeanDefinitions(
            AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry);

}
