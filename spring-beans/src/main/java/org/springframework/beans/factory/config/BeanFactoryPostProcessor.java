/*
 * Copyright 2002-2012 the original author or authors.
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

package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

/**
 * 允许自定义修改应用程序上下文的bean定义，调整上下文底层bean工厂的bean属性值。
 *
 * <p>应用程序上下文可以在它们的bean定义中自动检测BeanFactoryPostProcessor bean，并在创建任何其他bean之前应用它们。
 *
 * <p>对于覆盖在应用程序上下文中配置的bean属性的系统管理员的自定义配置文件很有用。
 *
 * <p>请参见PropertyResourceConfigurer及其具体实现，以获得解决此类配置需求的开箱即用解决方案。
 *
 * <p>一个BeanFactoryPostProcessor可以与bean定义交互并修改bean定义，但是从不与bean实例交互。
 * 这样做可能会导致过早的bean实例化，破坏容器并导致意外的副作用。如果需要bean实例交互，请考虑实现{@link BeanPostProcessor} 代替.
 *
 * @author Juergen Hoeller
 * @since 06.07.2003
 * @see BeanPostProcessor
 * @see PropertyResourceConfigurer
 */
public interface BeanFactoryPostProcessor {

	/**
	 * 在标准初始化之后修改应用程序上下文的内部bean工厂。
	 * 所有bean定义都已加载，但还没有实例化bean。这允许覆盖或添加属性，甚至可以在快速初始化bean中。
	 *
	 * @param beanFactory the bean factory used by the application context
	 * @throws org.springframework.beans.BeansException in case of errors
	 */
	void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;

}
