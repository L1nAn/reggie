package com.mzw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzw.dto.SetmealDto;
import com.mzw.entity.Setmeal;

import java.util.List;

/**
 * @author :马治伟
 * @version :1.0
 * @Date : 2023/4/19
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，同时保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联关系
     * @param ids
     */
    public void removeWithDish(List<Long>ids);
}
