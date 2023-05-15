package com.mzw;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class ReggieTackOutApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieTackOutApplication.class, args);
        //日志，在控制台输出日志，必须有注解
        log.info("项目启动成功");
    }
}
