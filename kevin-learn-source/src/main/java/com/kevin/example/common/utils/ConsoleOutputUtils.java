package com.kevin.example.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @ClassName: ConsoleOutput
 * @Author: kevin
 * @Time: 2020-03-20 20:06
 * @description: 内容输出
 **/
@Slf4j
public class ConsoleOutputUtils {

    private static int order = 1;

    public static void println(String content) {
        System.out.println(content);
    }
    public static void println(String content, Object ... args) {
        System.out.printf(content+"\n", args);
    }
    public static void hr() {
        hr("");
    }
    public static void hr(String title, String ... params) {
        hr(String.format(title, params));
    }
    public static void hr(String title) {
        if (StringUtils.isNotEmpty(title)) {
            title = order++ + title;
        }
        System.out.println("======================================================================"+ (Objects.isNull(title) ? "" : title) +"==========================================================================");
    }
    public static void hrl() {
        hrl("");
    }
    public static void hrl(String title, String ... params) {
        hrl(String.format(title, params));
    }
    public static void hrl(String title) {
        if (StringUtils.isNotEmpty(title)) {
            title = order++ + title;
        }
        System.out.println("**********************************************************************"+ (Objects.isNull(title) ? "" : title) +"**************************************************************************");
    }
}
