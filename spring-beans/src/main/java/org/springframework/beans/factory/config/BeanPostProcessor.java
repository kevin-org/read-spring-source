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
 * Factory hook that allows for custom modification of new bean instances,
 * e.g. checking for marker interfaces or wrapping them with proxies.
 *
 * <p>ApplicationContexts can autodetect BeanPostProcessor beans in their
 * bean definitions and apply them to any beans subsequently created.
 * Plain bean factories allow for programmatic registration of post-processors,
 * applying to all beans created through this factory.
 *
 * <p>Typically, post-processors that populate beans via marker interfaces
 * or the like will implement {@link #postProcessBeforeInitialization},
 * while post-processors that wrap beans with proxies will normally
 * implement {@link #postProcessAfterInitialization}.
 *
 * @author Juergen Hoeller
 * @since 10.10.2003
 * @see InstantiationAwareBeanPostProcessor
 * @see DestructionAwareBeanPostProcessor
 * @see ConfigurableBeanFactory#addBeanPostProcessor
 * @see BeanFactoryPostProcessor
 */
public interface BeanPostProcessor {

	/**
	 * 在</i>任何bean初始化回调之前，将这个BeanPostProcessor应用到给定的新bean实例<i>
	 *     中(如初始化bean的{@code afterPropertiesSet}或自定义的init方法)。bean已经填充了属性值。
	 *     返回的bean实例可能是原始bean的包装器。
	 * @param bean the new bean instance
	 * @param beanName the name of the bean
	 * @return the bean instance to use, either the original or a wrapped one;
	 * if {@code null}, no subsequent BeanPostProcessors will be invoked
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet
	 */
	Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

	/**
	 * 在</i>之后，将这个BeanPostProcessor应用到任何bean初始化回调(如初始化bean的{@code afterPropertiesSet}
	 * 或自定义的init方法)后的新bean实例<i>。bean已经填充了属性值。返回的bean实例可能是原始bean的包装器。
	 * <p>对于FactoryBean，这个回调将被同时调用FactoryBean实例和由FactoryBean创建的对象(从Spring 2.0开始)。
	 * 后处理器可以通过相应的{@code bean instanceof FactoryBean}检查来决定是应用到FactoryBean还是创建的对象
	 * ，或者两者都应用。<p>这个回调也会在一个
	 * {@link InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation}方法触发短路后被调用
	 * ，这与其他所有BeanPostProcessor回调形成对比。
	 *
	 * @param bean the new bean instance
	 * @param beanName the name of the bean
	 * @return the bean instance to use, either the original or a wrapped one;
	 * if {@code null}, no subsequent BeanPostProcessors will be invoked
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet
	 * @see org.springframework.beans.factory.FactoryBean
	 */
	Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;

}
