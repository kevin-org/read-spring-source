/*
 * Copyright 2002-2015 the original author or authors.
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
 * 添加销毁前回调的{@link BeanPostProcessor}的子接口。
 *
 * <p>典型的用法是调用特定bean类型的自定义销毁回调，匹配相应的初始化回调。
 *
 * @author Juergen Hoeller
 * @since 1.0.1
 */
public interface DestructionAwareBeanPostProcessor extends BeanPostProcessor {

	/**
	 * 在销毁给定bean实例之前，对其应用此BeanPostProcessor。可以调用自定义销毁回调。
	 * 与DisposableBean的{@code destroy}和自定义的destroy方法一样
	 * ，这个回调只适用于工厂中的单例bean(包括内部bean)。
	 *
	 * @param bean the bean instance to be destroyed
	 * @param beanName the name of the bean
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see org.springframework.beans.factory.DisposableBean
	 * @see org.springframework.beans.factory.support.AbstractBeanDefinition#setDestroyMethodName
	 */
	void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException;

	/**
	 * Determine whether the given bean instance requires destruction by this
	 * post-processor.
	 * <p><b>NOTE:</b> Even as a late addition, this method has been introduced on
	 * {@code DestructionAwareBeanPostProcessor} itself instead of on a SmartDABPP
	 * subinterface. This allows existing {@code DestructionAwareBeanPostProcessor}
	 * implementations to easily provide {@code requiresDestruction} logic while
	 * retaining compatibility with Spring <4.3, and it is also an easier onramp to
	 * declaring {@code requiresDestruction} as a Java 8 default method in Spring 5.
	 * <p>If an implementation of {@code DestructionAwareBeanPostProcessor} does
	 * not provide a concrete implementation of this method, Spring's invocation
	 * mechanism silently assumes a method returning {@code true} (the effective
	 * default before 4.3, and the to-be-default in the Java 8 method in Spring 5).
	 * @param bean the bean instance to check
	 * @return {@code true} if {@link #postProcessBeforeDestruction} is supposed to
	 * be called for this bean instance eventually, or {@code false} if not needed
	 * @since 4.3
	 */
	boolean requiresDestruction(Object bean);

}
