package com.mzw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mzw.common.R;
import com.mzw.dto.DishDto;
import com.mzw.entity.Category;
import com.mzw.entity.Dish;
import com.mzw.entity.DishFlavor;
import com.mzw.service.CategoryService;
import com.mzw.service.DishFlavorService;
import com.mzw.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author :马治伟
 * @version :1.0
 * @Date : 2023/4/24
 */

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());

        //因为要操纵两张表，所以方法要自己去定义
        dishService.saveWithFlavor(dishDto);

        return R.success("新增菜品成功");
    }

    /**
     * 菜品信息分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //构造分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //构造条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish::getName, name);//只有当名称不为空的时候才会进行查询
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);//根据更新时间降序来进行排行

        //执行分页查询
        dishService.page(pageInfo, queryWrapper);

        //dish里面存的是分类的id,但是页面要的是名称，所以我们不能直接返回结果，还需要进行再次完善代码。

        //对象拷贝，属性拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");//复制对象，除了数据，因为数据要单独处理。

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            //拷贝基本属性，出名称之外的属性，因为名称是另一张表上的
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();//分类id
            //根据id 查询分类对象，目的是要分类名称
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());//将处理的数据转为list集合
//        List<DishDto> list = null;
        dishDtoPage.setRecords(list);
//        return R.success(pageInfo);
        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息，所以要查两张表，所以这个方法要去service里面去扩展
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {//因为页面要回显的还有口味，但是口味在另一张表上，所以泛型采取DishDto这个类型
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        //因为更新涉及到两个表，口味表和菜品表，所以要扩展方法

        dishService.updateWithFlavor(dishDto);

        return R.success("修改菜品成功");
    }

    /**
     * 根据条件查询对用的菜品数据，前端页面是id
     *
     * @param dish
     * @return
     */
    /**   这是之前的方法
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish) {//用dish来接受请求参数更通用一些
        //构造条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //添加条件，状态为1，说明正在出售
        queryWrapper.eq(Dish::getStatus, 1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);

        return R.success(list);
    }
    */
    //这是为了购物车中显示口味的信息所改造的上边的代码
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {//用dish来接受请求参数更通用一些
        //构造条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //添加条件，状态为1，说明正在出售
        queryWrapper.eq(Dish::getStatus, 1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);

            //sql:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }
}
