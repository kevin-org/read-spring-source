/*
 * Copyright 2002-2014 the original author or authors.
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

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.AttributeAccessor;

/**
 * BeanDefinition描述一个bean实例，该实例具有属性值、构造函数参数值和由具体实现提供的进一步信息。
 *
 * <p>这只是一个最小的接口:主要目的是允许{@link BeanFactoryPostProcessor}
 * ，比如{@link PropertyPlaceholderConfigurer}来内省和修改属性值和其他bean元数据。
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @since 19.03.2004
 * @see ConfigurableListableBeanFactory#getBeanDefinition
 * @see org.springframework.beans.factory.support.RootBeanDefinition
 * @see org.springframework.beans.factory.support.ChildBeanDefinition
 */
public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {

	/**
	 * 标准单例范围的范围标识符:“单例”。<p>注意扩展的bean工厂可能支持进一步的范围。
	 * @see #setScope
	 */
	String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

	/**
	 * 标准原型范围标识符:“prototype”。注意扩展的bean工厂可能支持进一步的作用域。
	 * @see #setScope
	 */
	String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;


	/**
	 * 指示{@code BeanDefinition}是应用程序的主要部分的角色提示。通常对应于用户定义的bean。
	 */
	int ROLE_APPLICATION = 0;

	/**
	 * 角色提示，指示{@code BeanDefinition}是某个较大配置(通常是外部配置)的支持部分
	 * {@link org.springframework.beans.factory.parsing.ComponentDefinition}.
	 * {@code SUPPORT} bean被认为是非常重要的，因此在更仔细地观察某个特定的对象时可以意识到它的重要性
	 * {@link org.springframework.beans.factory.parsing.ComponentDefinition},
	 * 但在查看应用程序的总体配置时就不是这样了。
	 */
	int ROLE_SUPPORT = 1;

	/**
	 * 角色提示，指示{@code BeanDefinition}正在提供一个完全后台的角色，并且与最终用户没有关联。
	 * 当注册完全属于内部工作的bean时，会使用这个提示：
	 *  {@link org.springframework.beans.factory.parsing.ComponentDefinition}.
	 */
	int ROLE_INFRASTRUCTURE = 2;


	// 修改属性

	/**
	 * Set the name of the parent definition of this bean definition, if any.
	 */
	void setParentName(String parentName);

	/**
	 * Return the name of the parent definition of this bean definition, if any.
	 */
	String getParentName();

	/**
	 * Specify the bean class name of this bean definition.
	 * <p>The class name can be modified during bean factory post-processing,
	 * typically replacing the original class name with a parsed variant of it.
	 * @see #setParentName
	 * @see #setFactoryBeanName
	 * @see #setFactoryMethodName
	 */
	void setBeanClassName(String beanClassName);

	/**
	 * Return the current bean class name of this bean definition.
	 * <p>Note that this does not have to be the actual class name used at runtime, in
	 * case of a child definition overriding/inheriting the class name from its parent.
	 * Also, this may just be the class that a factory method is called on, or it may
	 * even be empty in case of a factory bean reference that a method is called on.
	 * Hence, do <i>not</i> consider this to be the definitive bean type at runtime but
	 * rather only use it for parsing purposes at the individual bean definition level.
	 * @see #getParentName()
	 * @see #getFactoryBeanName()
	 * @see #getFactoryMethodName()
	 */
	String getBeanClassName();

	/**
	 * Override the target scope of this bean, specifying a new scope name.
	 * @see #SCOPE_SINGLETON
	 * @see #SCOPE_PROTOTYPE
	 */
	void setScope(String scope);

	/**
	 * Return the name of the current target scope for this bean,
	 * or {@code null} if not known yet.
	 */
	String getScope();

	/**
	 * Set whether this bean should be lazily initialized.
	 * <p>If {@code false}, the bean will get instantiated on startup by bean
	 * factories that perform eager initialization of singletons.
	 */
	void setLazyInit(boolean lazyInit);

	/**
	 * Return whether this bean should be lazily initialized, i.e. not
	 * eagerly instantiated on startup. Only applicable to a singleton bean.
	 */
	boolean isLazyInit();

	/**
	 * Set the names of the beans that this bean depends on being initialized.
	 * The bean factory will guarantee that these beans get initialized first.
	 */
	void setDependsOn(String... dependsOn);

	/**
	 * Return the bean names that this bean depends on.
	 */
	String[] getDependsOn();

	/**
	 * 设置这个bean是否可以自动加载到其他bean中。<p>注意这个标志被设计为只影响基于类型的自动装配。
	 * 它不会影响名称的显式引用，即使指定的bean没有标记为自动装配候选对象，也会被解析。
	 * 因此，如果名称匹配，按名称自动装配仍然会注入一个bean。
	 */
	void setAutowireCandidate(boolean autowireCandidate);

	/**
	 * Return whether this bean is a candidate for getting autowired into some other bean.
	 */
	boolean isAutowireCandidate();

	/**
	 * Set whether this bean is a primary autowire candidate.
	 * <p>If this value is {@code true} for exactly one bean among multiple
	 * matching candidates, it will serve as a tie-breaker.
	 */
	void setPrimary(boolean primary);

	/**
	 * Return whether this bean is a primary autowire candidate.
	 */
	boolean isPrimary();

	/**
	 * Specify the factory bean to use, if any.
	 * This the name of the bean to call the specified factory method on.
	 * @see #setFactoryMethodName
	 */
	void setFactoryBeanName(String factoryBeanName);

	/**
	 * Return the factory bean name, if any.
	 */
	String getFactoryBeanName();

	/**
	 * Specify a factory method, if any. This method will be invoked with
	 * constructor arguments, or with no arguments if none are specified.
	 * The method will be invoked on the specified factory bean, if any,
	 * or otherwise as a static method on the local bean class.
	 * @see #setFactoryBeanName
	 * @see #setBeanClassName
	 */
	void setFactoryMethodName(String factoryMethodName);

	/**
	 * Return a factory method, if any.
	 */
	String getFactoryMethodName();

	/**
	 * Return the constructor argument values for this bean.
	 * <p>The returned instance can be modified during bean factory post-processing.
	 * @return the ConstructorArgumentValues object (never {@code null})
	 */
	ConstructorArgumentValues getConstructorArgumentValues();

	/**
	 * 返回应用于bean的新实例的属性值。<p>可以在bean工厂后处理期间修改返回的实例。
	 *
	 * @return MutablePropertyValues对象(never {@code null})
	 */
	MutablePropertyValues getPropertyValues();


	// 只读属性

	/**
	 * Return whether this a <b>Singleton</b>, with a single, shared instance
	 * returned on all calls.
	 * @see #SCOPE_SINGLETON
	 */
	boolean isSingleton();

	/**
	 * Return whether this a <b>Prototype</b>, with an independent instance
	 * returned for each call.
	 * @see #SCOPE_PROTOTYPE
	 */
	boolean isPrototype();

	/**
	 * 返回这个bean是否“抽象”，也就是说，是否意味着不需要实例化。
	 */
	boolean isAbstract();

	/**
	 * 获取这个{@code BeanDefinition}的角色提示。
	 * 角色提示为框架和工具提供了关于特定{@code BeanDefinition}的角色和重要性的指示。
	 *
	 * @see #ROLE_APPLICATION
	 * @see #ROLE_SUPPORT
	 * @see #ROLE_INFRASTRUCTURE
	 */
	int getRole();

	/**
	 * 返回这个bean定义的人类可读的描述。
	 */
	String getDescription();

	/**
	 * 返回这个bean定义所来自的资源的描述(用于在出现错误时显示上下文)。
	 */
	String getResourceDescription();

	/**
	 * 返回原始BeanDefinition，如果没有，则返回{@code null}。允许检索修饰bean定义(如果有的话)。
	 * <p>注意，此方法返回直接的发起者。遍历originator链以查找用户定义的原始BeanDefinition。
	 */
	BeanDefinition getOriginatingBeanDefinition();

}
