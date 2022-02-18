package com.kevin.example.main;

import com.kevin.example.common.beans.SimpleBean;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * @ClassName: XmlBeanGeneral
 * @Author: 丁海峰
 * @Time: 2020-05-01 13:14
 * @description:
 **/
public class XmlBeanGeneral {
    public static void main(String[] args) {
        ClassPathResource resource = new ClassPathResource("kevin/simple-bean.xml");
        XmlBeanFactory factory = new XmlBeanFactory(resource);
        SimpleBean simpleBean = (SimpleBean) factory.getBean("simpleBean");
        System.out.println(simpleBean.toString());
    }
}
