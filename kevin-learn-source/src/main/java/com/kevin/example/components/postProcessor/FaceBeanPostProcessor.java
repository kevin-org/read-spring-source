package com.kevin.example.components.postProcessor;

import com.kevin.example.common.utils.ConsoleOutputUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 实现BeanPostProcessor接口可以在Bean(实例化之后)初始化的前后做一些自定义的操作，但是拿到的参数只有BeanDefinition实例和BeanDefinition的名称，也就是无法修改BeanDefinition元数据
 * ,这里说的Bean的初始化是：
 *
 * 1）bean实现了InitializingBean接口，对应的方法为afterPropertiesSet
 *
 * 2）在bean定义的时候，通过init-method设置的方法
 *
 * 注: BeanFactoryPostProcessor回调会先于BeanPostProcessor
 **/
@Order(999)
@Component
public class FaceBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        ConsoleOutputUtils.hr("Bean后置处理 初始化前 %s -  BeanPostProcessor.postProcessBeforeInitialization", beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        ConsoleOutputUtils.hr("Bean后置处理 初始化后 %s -  BeanPostProcessor.postProcessAfterInitialization", beanName);
        return bean;
    }
}
