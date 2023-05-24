package com.mzw.dto;

import com.mzw.entity.Dish;
import com.mzw.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {//将所有菜品实体类的属性全部继承过来了。
    //同时又自己扩展了其他属性
    private List<DishFlavor> flavors = new ArrayList<>();//就是接受口味信息的，类型是集合是根据前端提交的数据类型导致的

    private String categoryName;//这个属性就是展示菜品数据的时候显示分类名称的，之前不显示，因为dish里面是菜品id，没有名称。

    private Integer copies;
}
