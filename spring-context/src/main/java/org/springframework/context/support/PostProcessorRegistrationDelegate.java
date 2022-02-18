/*
 * Copyright 2002-2017 the original author or authors.
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

package org.springframework.context.support;

import com.kevin.common.utils.print.ConsoleOutputUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * AbstractApplicationContext的后处理器处理的委托。
 *
 * @author Juergen Hoeller
 * @since 4.0
 */
class PostProcessorRegistrationDelegate {

	/**
	 * 调用Bean工厂后处理器
	 * @param beanFactory Bean工厂
	 * @param beanFactoryPostProcessors BeanFactory后置处理器列表
	 */
	public static void invokeBeanFactoryPostProcessors(
			ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {

		// 如果有的话，首先调用 BeanDefinitionRegistryPostProcessors。
		Set<String> processedBeans = new HashSet<String>();

		if (beanFactory instanceof BeanDefinitionRegistry) {
			//Bean定义注册表
			BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
			//定期发布后处理器
			List<BeanFactoryPostProcessor> regularPostProcessors = new LinkedList<BeanFactoryPostProcessor>();
			//注册后处理器
			List<BeanDefinitionRegistryPostProcessor> registryPostProcessors =
					new LinkedList<>();

			for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors) {
				if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
					//注册后处理器
					BeanDefinitionRegistryPostProcessor registryPostProcessor =
							(BeanDefinitionRegistryPostProcessor) postProcessor;
					//后置处理Bean定义注册表
					registryPostProcessor.postProcessBeanDefinitionRegistry(registry);
					registryPostProcessors.add(registryPostProcessor);
				}
				else {
					regularPostProcessors.add(postProcessor);
				}
			}

			// ！！！！要在这里初始化factorybean:我们需要保持所有常规bean未初始化，以便让bean工厂后处理器应用到它们!
			// 在BeanDefinitionRegistryPostProcessor之间的分离，实现优先排序，命令，和其他。
			String[] postProcessorNames =
					beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);

			// 首先，调用实现PriorityOrdered的BeanDefinitionRegistryPostProcessor。
			List<BeanDefinitionRegistryPostProcessor> priorityOrderedPostProcessors = new ArrayList<BeanDefinitionRegistryPostProcessor>();
			for (String ppName : postProcessorNames) {
				//优先排序的类型
				if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
					//通过 getBean 实例化BeanDefinitionRegistryPostProcessor
					priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
					//添加到已处理 列表
					processedBeans.add(ppName);
				}
			}
			//排序后处理器
			sortPostProcessors(beanFactory, priorityOrderedPostProcessors);
			registryPostProcessors.addAll(priorityOrderedPostProcessors);
			//调用Bean定义注册表后处理器（priorityOrderedPostProcessors）
			invokeBeanDefinitionRegistryPostProcessors(priorityOrderedPostProcessors, registry);

			// 接下来，调用实现Ordered的BeanDefinitionRegistryPostProcessors。
			postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
			List<BeanDefinitionRegistryPostProcessor> orderedPostProcessors = new ArrayList<BeanDefinitionRegistryPostProcessor>();
			for (String ppName : postProcessorNames) {
				//此处就不会添加 ConfigurationClassPostProcessor 了，因为上面已经处理过了
				if (!processedBeans.contains(ppName) && beanFactory.isTypeMatch(ppName, Ordered.class)) {
					orderedPostProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
					processedBeans.add(ppName);
				}
			}
			sortPostProcessors(beanFactory, orderedPostProcessors);
			registryPostProcessors.addAll(orderedPostProcessors);
			//调用其他 未处理过的 实现了 Ordered 接口的 BeanDefinitionRegistryPostProcessor 后处理器（orderedPostProcessors）
			invokeBeanDefinitionRegistryPostProcessors(orderedPostProcessors, registry);

			// 最后，调用所有其他BeanDefinitionRegistryPostProcessor，直到没有其他的出现为止。
			boolean reiterate = true;
			while (reiterate) {
				reiterate = false;
				postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
				for (String ppName : postProcessorNames) {
					if (!processedBeans.contains(ppName)) {
						BeanDefinitionRegistryPostProcessor pp = beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class);
						registryPostProcessors.add(pp);
						processedBeans.add(ppName);
						pp.postProcessBeanDefinitionRegistry(registry);
						reiterate = true;
					}
				}
			}

			// 现在，调用到目前为止处理的所有处理器的postProcessBeanFactory回调。
			invokeBeanFactoryPostProcessors(registryPostProcessors, beanFactory);
			invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);
		}

		else {
			// 调用上下文实例注册的工厂处理器。（BeanFactoryPostProcessor）
			invokeBeanFactoryPostProcessors(beanFactoryPostProcessors, beanFactory);
		}

		// ！！不要在这里初始化FactoryBean:我们需要保持所有常规bean未初始化，以便让bean工厂后处理器应用到它们!
		String[] postProcessorNames =
				beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);

		// 在BeanFactoryPostProcessor之间分离，实现优先排序、排序和其他。
		List<BeanFactoryPostProcessor> priorityOrderedPostProcessors = new ArrayList<BeanFactoryPostProcessor>();
		List<String> orderedPostProcessorNames = new ArrayList<String>();
		List<String> nonOrderedPostProcessorNames = new ArrayList<String>();
		for (String ppName : postProcessorNames) {
			if (processedBeans.contains(ppName)) {
				// 跳过——已经在上面的第一阶段处理
			}
			else if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
				priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanFactoryPostProcessor.class));
			}
			else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
				orderedPostProcessorNames.add(ppName);
			}
			else {
				nonOrderedPostProcessorNames.add(ppName);
			}
		}

		// 首先，调用实现 PriorityOrdered 的 BeanFactoryPostProcessor
		sortPostProcessors(beanFactory, priorityOrderedPostProcessors);
		invokeBeanFactoryPostProcessors(priorityOrderedPostProcessors, beanFactory);

		// 接下来，调用实现 Ordered 的 BeanFactoryPostProcessor。
		List<BeanFactoryPostProcessor> orderedPostProcessors = new ArrayList<BeanFactoryPostProcessor>();
		for (String postProcessorName : orderedPostProcessorNames) {
			orderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		sortPostProcessors(beanFactory, orderedPostProcessors);
		invokeBeanFactoryPostProcessors(orderedPostProcessors, beanFactory);

		// 最后，调用所有其他 BeanFactoryPostProcessor
		List<BeanFactoryPostProcessor> nonOrderedPostProcessors = new ArrayList<BeanFactoryPostProcessor>();
		for (String postProcessorName : nonOrderedPostProcessorNames) {
			nonOrderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		invokeBeanFactoryPostProcessors(nonOrderedPostProcessors, beanFactory);

		// 清除缓存的合并bean定义，因为后处理器可能修改了原始元数据，例如替换值中的占位符…
		beanFactory.clearMetadataCache();
	}

	public static void registerBeanPostProcessors(
			ConfigurableListableBeanFactory beanFactory, AbstractApplicationContext applicationContext) {

		String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);

		// 注册BeanPostProcessorChecker，当bean在BeanPostProcessor实例化过程中创建时
		// ，即当一个bean不适合被所有BeanPostProcessor处理时，记录一个信息消息。
		int beanProcessorTargetCount = beanFactory.getBeanPostProcessorCount() + 1 + postProcessorNames.length;
		beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory, beanProcessorTargetCount));

		// 在实现优先排序、有序和其他的BeanPostProcessor之间进行分离。
		List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<BeanPostProcessor>();
		List<BeanPostProcessor> internalPostProcessors = new ArrayList<BeanPostProcessor>();
		List<String> orderedPostProcessorNames = new ArrayList<String>();
		List<String> nonOrderedPostProcessorNames = new ArrayList<String>();
		for (String ppName : postProcessorNames) {

			if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
				//处理实现 PriorityOrdered 的 BeanPostProcessor
				BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
				priorityOrderedPostProcessors.add(pp);
				if (pp instanceof MergedBeanDefinitionPostProcessor) {
					//内部的Bean后处理器
					internalPostProcessors.add(pp);
				}
			}
			else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
				//处理实现 Ordered 的 BeanPostProcessor
				orderedPostProcessorNames.add(ppName);
			}
			else {
				//处理其他 BeanPostProcessor
				nonOrderedPostProcessorNames.add(ppName);
			}
		}

		// 首先，注册实现 PriorityOrdered 的BeanPostProcessor
		sortPostProcessors(beanFactory, priorityOrderedPostProcessors);
		registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors);

		// 接下来，注册实现 Ordered 的 BeanPostProcessors。
		List<BeanPostProcessor> orderedPostProcessors = new ArrayList<BeanPostProcessor>();
		for (String ppName : orderedPostProcessorNames) {
			BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
			orderedPostProcessors.add(pp);
			//合并Bean定义后处理器
			if (pp instanceof MergedBeanDefinitionPostProcessor) {
				internalPostProcessors.add(pp);
			}
		}
		sortPostProcessors(beanFactory, orderedPostProcessors);
		registerBeanPostProcessors(beanFactory, orderedPostProcessors);

		// 现在，注册所有常规 BeanPostProcessors。
		List<BeanPostProcessor> nonOrderedPostProcessors = new ArrayList<BeanPostProcessor>();
		for (String ppName : nonOrderedPostProcessorNames) {
			BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
			nonOrderedPostProcessors.add(pp);
			if (pp instanceof MergedBeanDefinitionPostProcessor) {
				internalPostProcessors.add(pp);
			}
		}
		//注册Bean后处理器（nonOrderedPostProcessors）
		registerBeanPostProcessors(beanFactory, nonOrderedPostProcessors);

		// 最后，重新注册所有内部 BeanPostProcessors.
		sortPostProcessors(beanFactory, internalPostProcessors);
		registerBeanPostProcessors(beanFactory, internalPostProcessors);

		// 重新注册后处理器来检测内部bean作为应用监听器，将它移到处理器链的末端(用于拾取代理等等)。
		beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(applicationContext));
	}

	private static void sortPostProcessors(ConfigurableListableBeanFactory beanFactory, List<?> postProcessors) {
		Comparator<Object> comparatorToUse = null;
		if (beanFactory instanceof DefaultListableBeanFactory) {
			comparatorToUse = ((DefaultListableBeanFactory) beanFactory).getDependencyComparator();
		}
		if (comparatorToUse == null) {
			comparatorToUse = OrderComparator.INSTANCE;
		}
		Collections.sort(postProcessors, comparatorToUse);
	}

	/**
	 * 调用给定的BeanDefinitionRegistryPostProcessor bean。
	 */
	private static void invokeBeanDefinitionRegistryPostProcessors(
			Collection<? extends BeanDefinitionRegistryPostProcessor> postProcessors, BeanDefinitionRegistry registry) {

		for (BeanDefinitionRegistryPostProcessor postProcessor : postProcessors) {
			postProcessor.postProcessBeanDefinitionRegistry(registry);
		}
	}

	/**
	 * 调用给定的BeanFactoryPostProcessor bean。
	 */
	private static void invokeBeanFactoryPostProcessors(
			Collection<? extends BeanFactoryPostProcessor> postProcessors, ConfigurableListableBeanFactory beanFactory) {

		for (BeanFactoryPostProcessor postProcessor : postProcessors) {
			postProcessor.postProcessBeanFactory(beanFactory);
		}
	}

	/**
	 * 注册给定的BeanPostProcessor bean。
	 */
	private static void registerBeanPostProcessors(
			ConfigurableListableBeanFactory beanFactory, List<BeanPostProcessor> postProcessors) {

		for (BeanPostProcessor postProcessor : postProcessors) {
			beanFactory.addBeanPostProcessor(postProcessor);
		}
	}


	/**
	 * BeanPostProcessor that logs an info message when a bean is created during
	 * BeanPostProcessor instantiation, i.e. when a bean is not eligible for
	 * getting processed by all BeanPostProcessors.
	 */
	private static class BeanPostProcessorChecker implements BeanPostProcessor {

		private static final Log logger = LogFactory.getLog(BeanPostProcessorChecker.class);

		private final ConfigurableListableBeanFactory beanFactory;

		private final int beanPostProcessorTargetCount;

		public BeanPostProcessorChecker(ConfigurableListableBeanFactory beanFactory, int beanPostProcessorTargetCount) {
			this.beanFactory = beanFactory;
			this.beanPostProcessorTargetCount = beanPostProcessorTargetCount;
		}

		@Override
		public Object postProcessBeforeInitialization(Object bean, String beanName) {
			ConsoleOutputUtils.hr("BeanPostProcessorChecker - postProcessBeforeInitialization");
			return bean;
		}

		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName) {
			ConsoleOutputUtils.hr("BeanPostProcessorChecker - postProcessAfterInitialization");
			if (bean != null && !(bean instanceof BeanPostProcessor) && !isInfrastructureBean(beanName) &&
					this.beanFactory.getBeanPostProcessorCount() < this.beanPostProcessorTargetCount) {
				if (logger.isInfoEnabled()) {
					logger.info("Bean '" + beanName + "' of type [" + bean.getClass().getName() +
							"] is not eligible for getting processed by all BeanPostProcessors " +
							"(for example: not eligible for auto-proxying)");
				}
			}
			return bean;
		}

		private boolean isInfrastructureBean(String beanName) {
			if (beanName != null && this.beanFactory.containsBeanDefinition(beanName)) {
				BeanDefinition bd = this.beanFactory.getBeanDefinition(beanName);
				return (bd.getRole() == RootBeanDefinition.ROLE_INFRASTRUCTURE);
			}
			return false;
		}
	}

}
