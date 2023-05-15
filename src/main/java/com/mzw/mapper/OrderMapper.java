package com.mzw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzw.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author :马治伟
 * @version :1.0
 * @Date : 2023/5/9
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
