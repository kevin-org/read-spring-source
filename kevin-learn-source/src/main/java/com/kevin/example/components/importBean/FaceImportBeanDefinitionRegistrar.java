package com.kevin.example.components.importBean;

import com.kevin.example.components.helpBean.KevinImportClass;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * ImportBeanDefinitionRegistrar接口不是直接注册Bean到IOC容器，它的执行时机比较早，准确的说更像是注册Bean的定义信息以便后面的Bean的创建。
 *
 * ImportBeanDefinitionRegistrar注解可以比较灵活的实现Bean定义信息的注册，实际上我们在使用的时候都是通过实现该接口来达到按照指定条件注册bean定义信息
 * ，来达到注入对应的Bean到IOC容器的目的，可以灵活的按照自己的逻辑注册，且可以通过注解来开启对应的功能。
 */
public class FaceImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, ApplicationContextAware {
    public static final String KEVIN_IMPORT_BEAN_NAME = "kevinImportBean";
    /**
     * 官方解释
     * 1.当处理Java编程式配置类(使用了@Configuration的类)的时候，ImportBeanDefinitionRegistrar接口的实现类可以注册额外的bean definitions;
     *
     * 2.ImportBeanDefinitionRegistrar接口的实现类必须提供给@Import注解或者是ImportSelector接口返回值
     *
     * 3.ImportBeanDefinitionRegistrar接口的实现类可能还会实现下面org.springframework.beans.factory.Aware接口中的一个或者多个，它们各自的方法优先于ImportBeanDefinitionRegistrar#registerBeanDefinitions被调用
     **/
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(KevinImportClass.class);
        registry.registerBeanDefinition(KEVIN_IMPORT_BEAN_NAME, builder.getBeanDefinition());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        ConsoleOutputUtils.hr(applicationContext.getBean("kevinClass", false).toString());
    }
}
