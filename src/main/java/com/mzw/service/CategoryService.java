package com.mzw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzw.entity.Category;

/**
 * @author :马治伟
 * @version :1.0
 * @Date : 2023/4/18
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
