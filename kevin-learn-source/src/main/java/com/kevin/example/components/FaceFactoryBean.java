package com.kevin.example.components;

import com.kevin.common.utils.json.JsonUtil;
import com.kevin.example.common.utils.ConsoleOutputUtils;
import com.kevin.example.components.helpBean.KevinFactoryBeanClass;
import com.kevin.example.components.helpBean.KevinPostClass;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 通过Spring容器的getBean()方法返回的不是FactoryBean本身，而是FactoryBean#getObject()方法所返回的对象，相当于FactoryBean#getObject()代理了getBean()方法
 *
 * 如果希望获取CarFactoryBean的实例，则需要在使用getBean(beanName) 方法时在beanName前显示的加上 "&" 前缀。
 */
@Order(999)
@Component
public class FaceFactoryBean implements FactoryBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * 返回由FactoryBean创建的bean实例
     */
    @Override
    public Object getObject() throws Exception {
        ConsoleOutputUtils.hr("FaceFactoryBean.getObject");
        return new KevinFactoryBeanClass();
    }

    /**
     * 返回FactoryBean创建的bean类型
     */
    @Override
    public Class<?> getObjectType() {
//        ConsoleOutputUtils.hr("FaceFactoryBean.getObjectType");
        return KevinFactoryBeanClass.class;
    }

    /**
     * 返回由FactoryBean创建的bean实例的作用域是singleton还是prototype。
     */
    @Override
    public boolean isSingleton() {
        ConsoleOutputUtils.hr("FaceFactoryBean.isSingleton");
        return false;
    }

    @PostConstruct
    public void init() throws Exception {
        KevinPostClass bean = applicationContext.getBean(KevinPostClass.class);
        ConsoleOutputUtils.hr("KevinPostClass: "+ JsonUtil.toString(bean));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
