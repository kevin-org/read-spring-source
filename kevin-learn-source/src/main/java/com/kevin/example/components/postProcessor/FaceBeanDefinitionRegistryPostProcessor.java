package com.kevin.example.components.postProcessor;

import com.kevin.example.common.utils.ConsoleOutputUtils;
import com.kevin.example.components.helpBean.KevinClass;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * BeanDefinitionRegistryPostProcessor 接口可以看作是BeanFactoryPostProcessor
 * 和ImportBeanDefinitionRegistrar的功能集合，既可以获取和修改BeanDefinition的元数据
 * ，也可以实现BeanDefinition的注册、移除等操作。
 **/
@Order(999)
@Component
public class FaceBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    public static final String KEVIN_BEAN_NAME = "kevinBean";

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ConsoleOutputUtils.hr("Bean定义注册后置处理 BeanDefinitionRegistryPostProcessor.postProcessBeanDefinitionRegistry");
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(KevinClass.class).getBeanDefinition();
        registry.registerBeanDefinition(KEVIN_BEAN_NAME, beanDefinition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //查看 FaceBeanFactoryPostProcessor
    }

}
