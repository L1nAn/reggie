package com.mzw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzw.dto.DishDto;
import com.mzw.entity.Dish;
import com.mzw.entity.DishFlavor;
import com.mzw.mapper.DishMapper;
import com.mzw.service.DishFlavorService;
import com.mzw.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;//因为要操纵口味表，所以把这个考这来。


    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDto
     */
    @Override
    @Transactional//要想事务控制生效，需要在启动类上添加注解@EnableTransactionManagement
    //涉及到多张表操作，所以要加入事务控制,来保证数据的一致性
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息道菜品表
        this.save(dishDto);//this是表示用的就是这个类的方法
        //菜品id,前面已经将信息保存了，所以就会给菜品id赋上值了，所以我们直接获取就可以了。
        Long dishID = dishDto.getId();

        //得到菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();

        //处理flavors集合，用stream 流的方式进行处理。ps:也可以用循环的方式进行处理。
        flavors.stream().map((item) -> {
           item.setDishId(dishID);
           return item;
        }).collect(Collectors.toList());
        //保存菜品口味数据道菜品口味表dish_flavor

//        dishFlavorService.saveBatch(dishDto.getFlavors());
        dishFlavorService.saveBatch(flavors);
    }

    /**
     *
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);
        log.info("this代表的类是" + this);

        DishDto dishDto = new DishDto();
        //类拷贝，把dish的普通属性全部拷贝过来，然后只剩下口味属性没有赋值，然后根据后面查询到的是信息再次对dishDto进行赋值。
        BeanUtils.copyProperties(dish, dishDto);

        //查询当前菜品对应的口味信息，从dish_flavor表中查询
        LambdaQueryWrapper<DishFlavor> qu = new LambdaQueryWrapper<>();
        qu.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(qu);
        //进行口味信息设置，对应上面，由于拷贝的时候没有口味这个属性，所以要单独设置
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    /**
     * 更改数据
     * @param dishDto
     */
    @Override
    @Transactional//事务注解，保持数据一致性。
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);//因为dishDto 是 dish的子类，所以 这就是更新关于dish的属性
        //清理当前菜品对应口味数据dish_flavor的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());

        dishFlavorService.remove(queryWrapper);
        //添加当前提交过来的口味数据 --dish_flavor表的insert操作
        //如果没有修改也没有关系，就是把原来的数据再重新提交一遍就可以了。
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        //上边的这几行语句的意思是，flavors封装是只是封装的口味名称还有他的数据，并没有菜品的id，所以之后要加入菜品id
        //所以进行一个循环，将flavors赋予item，然后item进行操作。最后返回item，再重新给flavors赋值即可。
        dishFlavorService.saveBatch(flavors);
    }
}
