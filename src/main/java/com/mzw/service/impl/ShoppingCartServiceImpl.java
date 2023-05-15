package com.mzw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzw.entity.ShoppingCart;
import com.mzw.mapper.ShoppingCartMapper;
import com.mzw.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author :马治伟
 * @version :1.0
 * @Date : 2023/5/9
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
