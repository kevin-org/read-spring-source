package com.kevin.example.components.hook;

import com.kevin.example.common.utils.ConsoleOutputUtils;
import com.kevin.example.components.oberver.FaceApplicationEvent;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 1、Aware接口族
 * Spring中提供了各种Aware接口，方便从上下文中获取当前的运行环境，比较常见的几个子接口有：
 * BeanFactoryAware,BeanNameAware,ApplicationContextAware,EnvironmentAware，BeanClassLoaderAware等，这些Aware的作用都可以从命名得知，并且其使用也是十分简单。
 * 例如我们经常看到SpringContext工具类：
 * 实现ApplicationContextAware接口可以获取ApplicationContext
 * 又例如想获取到当前的一个Spring Bean的BeanFactory：
 * 一般来说，拿到的应该是DefaultListableBeanFactory，因为这个BeanFactory是BeanFactory一族的最底层的BeanFactory实现类，拥有所有父BeanFactory的功能。
 */
@Component
public class FaceAware implements BeanFactoryAware
        , BeanNameAware, ApplicationContextAware, EnvironmentAware, BeanClassLoaderAware {
    private BeanFactory beanFactory;
    private String name;
    private ApplicationContext applicationContext;
    private Environment environment;
    private ClassLoader classLoader;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        ConsoleOutputUtils.hr("钩子 - BeanFactoryAware.setBeanFactory");
        this.beanFactory = beanFactory;
    }

    @Override
    public void setBeanName(String name) {
        ConsoleOutputUtils.hr("钩子 - BeanNameAware.setBeanName");
        this.name = name;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ConsoleOutputUtils.hr("钩子 - ApplicationContextAware.setApplicationContext");
        this.applicationContext = applicationContext;
        ConsoleOutputUtils.hr("ApplicationContextAware.setApplicationContext 之后 发布Kevin自定义事件！！");
        applicationContext.publishEvent(new FaceApplicationEvent(applicationContext));
    }

    @Override
    public void setEnvironment(Environment environment) {
        ConsoleOutputUtils.hr("钩子 - EnvironmentAware.setEnvironment");
        this.environment = environment;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        ConsoleOutputUtils.hr("钩子 - BeanClassLoaderAware.setBeanClassLoader");
        this.classLoader = classLoader;
    }

}
