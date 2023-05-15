package com.mzw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzw.entity.Orders;

/**
 * @author :马治伟
 * @version :1.0
 * @Date : 2023/5/9
 */
public interface OrderService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);
}
