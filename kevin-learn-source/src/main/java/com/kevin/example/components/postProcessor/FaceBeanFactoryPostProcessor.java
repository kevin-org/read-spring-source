package com.kevin.example.components.postProcessor;

import com.kevin.example.common.utils.ConsoleOutputUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * BeanFactoryPostProcessor可以对bean的定义（配置元数据）进行处理。
 * 也就是说，Spring IoC容器允许BeanFactoryPostProcessor在  容器实际实例化 任何 其它的bean之前  读取配置元数据，并有可能修改它。
 */
@Order(999)
@Component
public class FaceBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ConsoleOutputUtils.hr("Bean工厂后置处理 BeanDefinitionRegistryPostProcessor - BeanFactoryPostProcessor.postProcessBeanFactory");
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition(FaceBeanDefinitionRegistryPostProcessor.KEVIN_BEAN_NAME);
        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
        propertyValues.add("name", "name - change");
    }

}
