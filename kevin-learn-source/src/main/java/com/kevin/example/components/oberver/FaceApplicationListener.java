package com.kevin.example.components.oberver;

import com.kevin.example.common.utils.ConsoleOutputUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 1. ApplicationListener是一个接口，里面只有一个onApplicationEvent(E event)方法，这个泛型E必须是ApplicationEvent的子类
 * ，而ApplicationEvent是Spring定义的事件，继承于EventObject，构造要求必须传入一个Object类型的source，这个source可以作为一个存储对象。
 *
 * 2. 将会在ApplicationListener的onApplicationEvent里面得到回调。如果在上下文中部署一个实现了ApplicationListener接口的bean
 * ，那么每当在一个ApplicationEvent发布到 ApplicationContext时，这个bean得到通知。
 *
 * 3. 其实这就是标准的Oberver设计模式。另外，ApplicationEvent的发布由ApplicationContext通过#publishEvent方法完成。
 */
@Component
public class FaceApplicationListener implements ApplicationListener<FaceApplicationEvent> {

    @Override
    public void onApplicationEvent(FaceApplicationEvent event) {
        ConsoleOutputUtils.hr("监听到 Kevin 发布的事件：" + event.getSource());
    }

}
