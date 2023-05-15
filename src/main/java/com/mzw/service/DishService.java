package com.mzw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzw.dto.DishDto;
import com.mzw.entity.Dish;

/**
 * @author :马治伟
 * @version :1.0
 * @Date : 2023/4/19
 */
public interface DishService extends IService<Dish> {

    //新增菜品，同时插入对应的口味数据，需要操纵两张表：dish 、dish-flavor
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品信息，同时更新对应的口味信息
    public void updateWithFlavor(DishDto dishDto);
}