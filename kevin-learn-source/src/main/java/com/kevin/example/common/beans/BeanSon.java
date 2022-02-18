package com.kevin.example.common.beans;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName: SimpleBean
 * @Author: kevin
 * @Time: 2020-02-08 12:16
 * @description:
 **/
@Data
@Component
public class BeanSon {
    @Resource
    private BeanParent beanParent;

    private String beanName = "son";
}
