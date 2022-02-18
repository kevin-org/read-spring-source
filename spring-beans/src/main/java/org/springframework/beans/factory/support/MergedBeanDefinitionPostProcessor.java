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

package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 运行时<i>合并</i> bean定义的后处理器回调接口。{@code BeanFactory}实现可以实现这个子接口，
 * 以便后期处理合并的bean定义(原始bean定义的处理副本)，Spring {@code BeanFactory}使用该定义创建一个bean实例。
 *
 * <p> {@link #postProcessMergedBeanDefinition}方法可以内省bean定义，以便在对bean的实际实例进行后处理之前准备一些缓存的元数据。
 * 它也允许修改bean定义，但<i>仅</i>用于实际用于并发修改的定义属性。本质上，这只适用于在{@link RootBeanDefinition}本身上定义的操作，而不适用于其基类的属性。
 *
 * @author Juergen Hoeller
 * @since 2.5
 * @see org.springframework.beans.factory.config.ConfigurableBeanFactory#getMergedBeanDefinition
 */
public interface MergedBeanDefinitionPostProcessor extends BeanPostProcessor {

	/**
	 * 对指定bean的给定合并bean定义进行后期处理。
	 *
	 * @param beanDefinition the merged bean definition for the bean
	 * @param beanType the actual type of the managed bean instance
	 * @param beanName the name of the bean
	 */
	void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName);

}
