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

package org.springframework.context.annotation;

import org.springframework.beans.factory.config.BeanDefinition;

/**
 * 用于解析bean定义的范围的策略接口
 *
 * @author Mark Fisher
 * @since 2.5
 * @see Scope
 */
public interface ScopeMetadataResolver {

	/**
	 * 解析适合于提供的bean {@code definition}的{@link ScopeMetadata}
	 *
	 * <p>实现当然可以使用他们喜欢的任何策略来确定作用域元数据，但一些实现立即想到的可能是使用
	 * 提供的{@code definition}的{@link BeanDefinition#getBeanClassName() class}上的源级注释
	 * ，或者使用提供的{@code definition}的{@link BeanDefinition#attributeNames()}中的元数据。
	 *
	 * @param definition the target bean definition
	 * @return the relevant scope metadata; never {@code null}
	 */
	ScopeMetadata resolveScopeMetadata(BeanDefinition definition);

}
