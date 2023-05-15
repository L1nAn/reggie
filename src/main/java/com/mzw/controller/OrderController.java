package com.mzw.controller;

import com.mzw.common.R;
import com.mzw.entity.Orders;
import com.mzw.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :马治伟
 * @version :1.0
 * @Date : 2023/5/9
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {//用户id不需要传递，因为登陆的时候我们随时可以获得用户的id
        log.info("订单数据：{}", orders);
        orderService.submit(orders);
        return null;
    }

}
