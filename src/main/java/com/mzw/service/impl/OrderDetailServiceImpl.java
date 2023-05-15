package com.mzw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzw.entity.OrderDetail;
import com.mzw.mapper.OrderDetailMapper;
import com.mzw.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author :马治伟
 * @version :1.0
 * @Date : 2023/5/9
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
