package org.springframework.kevin.beans;

import org.springframework.stereotype.Service;

/**
 * @author dinghaifeng
 * @date 2020-12-01 13:39:49
 * @desc
 */
@Service
public class AllTypeBeanImpl implements AllTypeBean {

    @Override
    public void abstractPrint() {
        System.out.println("i am simpleBean");
    }

    public void print() {
        System.out.println("i am simpleBean");
    }
}
