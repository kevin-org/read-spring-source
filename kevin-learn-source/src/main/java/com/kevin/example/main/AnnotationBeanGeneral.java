package com.kevin.example.main;

import com.kevin.example.common.beans.BeanSon;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @ClassName: AnnotationBeanGeneral
 * @Author: 丁海峰
 * @Time: 2020-05-01 13:14
 * @description:
 **/
public class AnnotationBeanGeneral {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("com.kevin.example.common.beans");
        BeanSon beanSon = context.getBean(BeanSon.class);
        System.out.println("Parent name: " + beanSon.getBeanParent().getBeanName());
        System.out.println("Son name: " + beanSon.getBeanName());
    }
}
