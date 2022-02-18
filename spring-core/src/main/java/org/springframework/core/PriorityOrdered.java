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

package org.springframework.core;

/**
 * 扩展了{@link Ordered}接口，表示<em>priority</em>排序:{@code PriorityOrdered}
 * 对象表示的顺序值总是在<em>plain</em> {@link Ordered}对象表示的相同顺序值之前。
 *
 * <p>这主要是一个特殊用途的接口，用于对象，其中  特别重要   的是识别<em>优先级</em>对象
 * ，甚至不需要获得剩余的对象。一个典型的例子是:
 * 在Spring {@link org.springframework.context.ApplicationContext}中对后处理器排序。
 *
 * <p>注意:{@code PriorityOrdered}后处理器bean被初始化在一个特殊的阶段，在其他后处理器bean之前。
 * 这微妙地影响了它们的自动装配行为: 它们将只对不需要为类型匹配进行初始化的bean进行自动装配。
 *
 * @author Juergen Hoeller
 * @since 2.5
 * @see org.springframework.beans.factory.config.PropertyOverrideConfigurer
 * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
 */
public interface PriorityOrdered extends Ordered {

}
