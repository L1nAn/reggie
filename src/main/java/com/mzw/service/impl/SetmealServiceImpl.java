package com.mzw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzw.common.CustomException;
import com.mzw.dto.SetmealDto;
import com.mzw.entity.Setmeal;
import com.mzw.entity.SetmealDish;
import com.mzw.mapper.SetmealMapper;
import com.mzw.service.SetmealDishService;
import com.mzw.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author :马治伟
 * @version :1.0
 * @Date : 2023/4/19
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;
    /**
     * 新增套餐，同时保存套餐和菜品的关联关系
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);//因为setmealDto是继承了Setmeal，所以直接save保存基础信息就可以了
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();//这集合里面是没有套餐id的，只有菜品的id，所以要加上
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联关系，操作setmeal_dish,执行insert操作
        setmealDishService.saveBatch(setmealDishes);

    }
    /**
     * 删除套餐，同时需要删除套餐和菜品的关联关系
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //select count(*) from setmeal where id in(1,2,3) and status = 1;
        //查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);

        int count = this.count(queryWrapper);
        if(count > 0) {
            //如果不能删除，抛出一个业务异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        //如果可以删除，先删除套餐表中的数据----setmeal
        this.removeByIds(ids);

        //delete from setmeal_dish where setmeal_id in (1,2,3)
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //上面的ids是套餐的id,但是在这需要删除的是菜品，所以还需要再次进行完善代码
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);//着的意思就是根据ids中套餐id来删除菜品
        //删除关系表中的数据---setmeal_dish
        setmealDishService.remove(lambdaQueryWrapper);
    }
}
