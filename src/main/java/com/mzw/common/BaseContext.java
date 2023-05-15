package com.mzw.common;

/**
 * @author :马治伟
 * @version :1.0
 * @Date : 2023/4/18
 */

/**
 * 基于ThreadLocal 封装工具类，用于获取和保存当前用户登录id
 * 作用范围：一个线程之内
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置值
     * @param id
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * 获取值
     * @return
     */
    public static long getCurrentId() {
        return threadLocal.get();
    }
}
