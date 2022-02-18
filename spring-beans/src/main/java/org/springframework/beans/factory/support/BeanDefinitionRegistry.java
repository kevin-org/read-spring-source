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

package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.AliasRegistry;

/**
 * 为持有bean定义(例如RootBeanDefinition和ChildBeanDefinition实例)的注册中心提供接口。
 * 通常由内部使用AbstractBeanDefinition层次结构的BeanFactories实现。
 *
 * 这是Spring的bean工厂包中封装bean定义<i>注册</i>的唯一接口。
 * 标准的BeanFactory接口只涉及对<i>完全配置的工厂实例</i>的访问。
 *
 * <p>Spring的bean定义读者希望工作在这个接口的实现上。
 * Spring核心中已知的实现者是DefaultListableBeanFactory和GenericApplicationContext。
 *
 * @author Juergen Hoeller
 * @since 26.11.2003
 * @see BeanDefinition
 * @see AbstractBeanDefinition
 * @see RootBeanDefinition
 * @see ChildBeanDefinition
 * @see DefaultListableBeanFactory
 * @see org.springframework.context.support.GenericApplicationContext
 * @see org.springframework.beans.factory.xml.XmlBeanDefinitionReader
 * @see PropertiesBeanDefinitionReader
 */
public interface BeanDefinitionRegistry extends AliasRegistry {

	/**
	 * 在这个注册中心注册一个新的bean定义。必须支持RootBeanDefinition和ChildBeanDefinition。
	 *
	 * @param beanName the name of the bean instance to register
	 * @param beanDefinition definition of the bean instance to register
	 * @throws BeanDefinitionStoreException if the BeanDefinition is invalid
	 * or if there is already a BeanDefinition for the specified bean name
	 * (and we are not allowed to override it)
	 * @see RootBeanDefinition
	 * @see ChildBeanDefinition
	 */
	void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
			throws BeanDefinitionStoreException;

	/**
	 * 删除给定名称的BeanDefinition。
	 *
	 * @param beanName the name of the bean instance to register
	 * @throws NoSuchBeanDefinitionException if there is no such bean definition
	 */
	void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

	/**
	 * 返回给定bean名称的BeanDefinition。
	 *
	 * @param beanName name of the bean to find a definition for
	 * @return the BeanDefinition for the given name (never {@code null})
	 * @throws NoSuchBeanDefinitionException if there is no such bean definition
	 */
	BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

	/**
	 * 检查这个注册表是否包含具有给定名称的bean定义。
	 *
	 * @param beanName the name of the bean to look for
	 * @return if this registry contains a bean definition with the given name
	 */
	boolean containsBeanDefinition(String beanName);

	/**
	 * 返回在这个注册表中定义的所有bean的名称。
	 *
	 * @return the names of all beans defined in this registry,
	 * or an empty array if none defined
	 */
	String[] getBeanDefinitionNames();

	/**
	 * 返回注册中心中定义的bean的数量。
	 *
	 * @return the number of beans defined in the registry
	 */
	int getBeanDefinitionCount();

	/**
	 * 确定给定的bean名称是否已经在注册中心中使用，即是否有本地bean或别名在该名称下注册。
	 *
	 * @param beanName the name to check
	 * @return whether the given bean name is already in use
	 */
	boolean isBeanNameInUse(String beanName);

}
