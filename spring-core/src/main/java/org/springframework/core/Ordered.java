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
 * {@code Ordered}是一个可以由应该是<em>Orderable</em>的对象实现的接口，例如在{@code集合}中。
 *
 * <p>实际的{@link #getOrder() order}可以解释为优先级，第一个对象(具有最低的顺序值)具有最高的优先级。
 *
 * <p>注意，这个接口还有一个<em>priority</em>标记:{@link PriorityOrdered}。
 * {@code PriorityOrdered}对象表示的顺序值总是应用在<em>plain</em> {@link Ordered}对象表示的相同顺序值之前。
 *
 * <p>查询Javadoc的{@link OrderComparator}以获得关于非有序对象的排序语义的详细信息。
 *
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 07.04.2003
 * @see PriorityOrdered
 * @see OrderComparator
 * @see org.springframework.core.annotation.Order
 * @see org.springframework.core.annotation.AnnotationAwareOrderComparator
 */
public interface Ordered {

	/**
	 * 用于最高优先级值的有用常数。
	 * @see Integer#MIN_VALUE
	 */
	int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

	/**
	 * 用于最低优先级值的有用常数。
	 * @see Integer#MAX_VALUE
	 */
	int LOWEST_PRECEDENCE = Integer.MAX_VALUE;


	/**
	 * 获取此对象的order值。<p>较高的值被解释为较低优先级。
	 * 因此，值最低的对象优先级最高(有点类似于Servlet {@code load-on-startup}值)。
	 * <p>相同的顺序值将导致受影响对象的任意排序位置。
	 *
	 * @return the order value
	 * @see #HIGHEST_PRECEDENCE
	 * @see #LOWEST_PRECEDENCE
	 */
	int getOrder();

}
