package com.mzw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mzw.common.R;
import com.mzw.entity.Category;
import com.mzw.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author :马治伟
 * @version :1.0
 * @Date : 2023/4/18
 */

/**
 * 分类管理
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //前端只用到一个code 所以返回String 就可以了
    //这样测试会报错，因为走的全局的异常处理器，报错的原因是会有重复的分类名称才会报错
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("category:{}", category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     *
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        //分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //条件构造器，这没有进行条件查询，创造这个类时专门用来进行排序操作的
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件，根据Category::getSort 进行排序。
        queryWrapper.orderByAsc(Category::getSort);//升序

        //进行分页查询
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id删除信息
     * @param id
     * @return
     */
    @DeleteMapping
    //删除返回一个泛型为String就可以了，因为R对象本身里面就有code，所以只需返回一个字符串即可
    public R<String> delete(Long id) {
        log.info("删除分类，id为：{}", id);

//        categoryService.removeById(ids);这是mybatis-plus默认的方法，我们需要对他进行改造
        categoryService.remove(id);

        return R.success("分类信息删除成功");
    }

    /**
     * 根据id修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("修改分类信息：{}", category);
        categoryService.updateById(category);
        //更新时间字段会自动更新，因为之前配置了
        return R.success("修改分类信息成功");
    }

    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {//这前端页面传过来的是type=1，所以是菜品分类，也可以用
                                                                // String type ，但是Category category实体类实用性更强
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        //添加排序条件，当sort相同的时候，在采用修改时间降序排序
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
