package com.kevin.example.common.beans;

import lombok.Data;

/**
 * @ClassName: SimpleBean
 * @Author: kevin
 * @Time: 2020-02-08 12:16
 * @description:
 **/
@Data
public class SimpleBean {
    /**
     * bean名称
     */
    private String beanName;
    /**
     * bean描述
     */
    private String beanDesc;
}
