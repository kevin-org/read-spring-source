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

package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;

import java.beans.PropertyDescriptor;

/**
 * Inner use
 *
 * {@link BeanPostProcessor}的子接口，添加了实例化前的回调，以及实例化后但在显式属性设置或自动装配发生之前的回调。
 *
 * <p>通常用于抑制特定目标bean的默认实例化，例如创建具有特殊目标源(池化目标、延迟初始化目标等)的代理
 * ，或实现额外的注入策略，如字段注入。
 *
 * 注:</b>此接口是一个专用接口，主要用于框架内部使用。建议尽可能实现普通的{@link BeanPostProcessor}接口
 * ，或者从{@link InstantiationAwareBeanPostProcessorAdapter}派生，以便屏蔽对该接口的扩展。
 *
 * @author Juergen Hoeller
 * @author Rod Johnson
 * @since 1.2
 * @see org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#setCustomTargetSourceCreators
 * @see org.springframework.aop.framework.autoproxy.target.LazyInitTargetSourceCreator
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

	/**
	 * 在目标bean实例化</i>之前应用这个BeanPostProcessor <i>。
	 * 返回的bean对象可以是替代目标bean使用的代理，从而有效地抑制目标bean的默认实例化。
	 *
	 * 如果该方法返回一个非空对象，则bean创建过程将被短路。唯一应用的进一步处理是来自配置的{@link BeanPostProcessor BeanPostProcessors}
	 * 的{@link #postProcessAfterInitialization}回调。
	 *
	 * <p>这个回调将只应用于具有bean类的bean定义。特别是，它不会应用于具有“工厂方法”的bean。
	 * <p>后处理器可以实现扩展的{@link SmartInstantiationAwareBeanPostProcessor}接口，以预测他们将返回的bean对象的类型。
	 *
	 * @param beanClass the class of the bean to be instantiated
	 * @param beanName the name of the bean
	 * @return 要公开的bean对象，而不是目标bean的默认实例，或者要继续进行默认实例化的{@code null}
	 *
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see org.springframework.beans.factory.support.AbstractBeanDefinition#hasBeanClass
	 * @see org.springframework.beans.factory.support.AbstractBeanDefinition#getFactoryMethodName
	 */
	Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

	/**
	 * 通过构造函数或工厂方法，在实例化bean之后执行操作，但是在Spring属性填充(从显式属性或自动装配)发生之前执行操作。
	 * <p>这是在给定bean实例上执行字段注入的理想回调。
	 * See Spring's own {@link org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor}
	 * for a typical example.
	 * @param bean the bean instance created, with properties not having been set yet
	 * @param beanName the name of the bean
	 * @return {@code true} if properties should be set on the bean; {@code false}
	 * if property population should be skipped. Normal implementations should return {@code true}.
	 * Returning {@code false} will also prevent any subsequent InstantiationAwareBeanPostProcessor
	 * instances being invoked on this bean instance.
	 * @throws org.springframework.beans.BeansException in case of errors
	 */
	boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException;

	/**
	 * 在工厂将给定的属性值应用到给定bean之前，对它们进行后处理。允许检查是否满足所有依赖
	 * ，例如基于bean属性设置器上的“Required”注释。<p>还允许替换要应用的属性值
	 * ，通常是通过基于原始的PropertyValues创建一个新的MutablePropertyValues实例，添加或删除特定的值。
	 *
	 * @param pvs the property values that the factory is about to apply (never {@code null})
	 * @param pds the relevant property descriptors for the target bean (with ignored
	 * dependency types - which the factory handles specifically - already filtered out)
	 * @param bean the bean instance created, but whose properties have not yet been set
	 * @param beanName the name of the bean
	 * @return the actual property values to apply to the given bean
	 * (can be the passed-in PropertyValues instance), or {@code null}
	 * to skip property population
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see org.springframework.beans.MutablePropertyValues
	 */
	PropertyValues postProcessPropertyValues(
            PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException;

}
