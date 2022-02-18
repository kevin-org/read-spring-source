package org.springframework.kevin;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.kevin.beans.AllTypeBean;
import org.springframework.kevin.beans.AllTypeBeanImpl;

/**
 * @author dinghaifeng
 * @date 2020-12-01 13:35:55
 * @desc
 */
public class DebugDoor {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext("org.springframework.kevin");
//        SimpleBean bean = context.getBean(SimpleBean.class);
        AllTypeBean bean = context.getBean(AllTypeBean.class);
        bean.abstractPrint();
        ((AllTypeBeanImpl)bean).print();
    }
}
