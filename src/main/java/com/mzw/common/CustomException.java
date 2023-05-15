package com.mzw.common;

/**
 * @author :马治伟
 * @version :1.0
 * @Date : 2023/4/19
 */

/**
 * 自定义业务异常
 */
public class CustomException extends RuntimeException{
    public CustomException(String message) {
        super(message);
    }
}
