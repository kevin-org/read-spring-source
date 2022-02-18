/*
 * Copyright 2002-2010 the original author or authors.
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

package org.springframework.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;

/**
 * 扩展到标准的{@link BeanFactoryPostProcessor} SPI，允许在
 * </i>常规BeanFactoryPostProcessor检测开始之前注册更多的bean定义<i>。
 * 特别是，BeanDefinitionRegistryPostProcessor可以注册更多的bean定义
 * ，这些定义反过来又定义了BeanFactoryPostProcessor实例。
 *
 * @author Juergen Hoeller
 * @since 3.0.1
 * @see org.springframework.context.annotation.ConfigurationClassPostProcessor
 */
public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor {

	/**
	 * 在标准初始化之后修改应用程序上下文的内部bean定义注册表。
	 * 所有常规bean定义都已加载，但还没有实例化bean。
	 * 这允许在进入下一个后处理阶段之前添加更多的bean定义。
	 *
	 * @param registry 应用程序上下文使用的bean定义注册表
	 * @throws org.springframework.beans.BeansException in case of errors
	 */
	void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException;

}
