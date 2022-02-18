package com.kevin.example.common.beans;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName: SimpleBean
 * @Author: kevin
 * @Time: 2020-02-08 12:16
 * @description:
 **/
@Data
@Component
public class BeanParent {
    /**
     * bean名称
     */
    private String beanName = "parent";
    /**
     * bean名称
     */
    @Value("${com.kevin.value:tmpValue}")
    private String atValue;
}
