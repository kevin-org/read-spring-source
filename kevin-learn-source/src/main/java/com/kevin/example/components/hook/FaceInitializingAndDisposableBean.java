package com.kevin.example.components.hook;

import com.kevin.example.common.utils.ConsoleOutputUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 2、InitializingBean接口和DisposableBean接口
 *
 * InitializingBean接口只有一个方法#afterPropertiesSet，作用是：当一个Bean实现InitializingBean
 * ，#afterPropertiesSet方法里面可以添加自定义的初始化方法或者做一些资源初始化操作(Invoked by a BeanFactory after
 * it has set all bean properties supplied ==> "当BeanFactory 设置完所有的Bean属性之后才会调用#afterPropertiesSet方法")。
 *
 * DisposableBean接口只有一个方法#destroy，作用是：当一个单例Bean实现DisposableBean，#destroy可以
 * 添加自定义的一些销毁方法或者资源释放操作(Invoked by a BeanFactory on destruction of a singleton ==>
 * "单例销毁时由BeanFactory调用#destroy")
 */
@Component
public class FaceInitializingAndDisposableBean implements InitializingBean, DisposableBean {

    @Override
    public void destroy() throws Exception {
        ConsoleOutputUtils.hr("调用销毁方法：DisposableBean.destroy");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ConsoleOutputUtils.hr("调用属性设置方法：InitializingBean.afterPropertiesSet");
    }
}
