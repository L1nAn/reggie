package com.mzw.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzw.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author :马治伟
 * @version :1.0
 * @Date : 2023/4/19
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
