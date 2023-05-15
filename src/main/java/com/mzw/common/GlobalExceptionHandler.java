package com.mzw.common;

/**
 * @author :马治伟
 * @version :1.0
 * @Date : 2023/4/17
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 *
 * 全局异常处理
 */
//这个注解就是通知的意思。。。。这个注解就是把注解为数组里面的类的异常捕获，就是加了下面两个注解（@RestController,@Controller）都会被处理
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody//返回（响应）一个JSON数据，所以要加这个注解
@Slf4j
public class GlobalExceptionHandler {
    /**
     *
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)//说明就是处理这类异常
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception) {
        log.info(exception.getMessage());

        //对异常信息进行判断，比如数据库重复添加
        if(exception.getMessage().contains("Duplicate entry")) {
            String[] split = exception.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)//说明就是处理这类异常
    public R<String> exceptionHandler(CustomException exception) {
        log.info(exception.getMessage());

        return R.error(exception.getMessage());
    }
}
