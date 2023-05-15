package com.mzw.dto;

import com.mzw.entity.Setmeal;
import com.mzw.entity.SetmealDish;
import com.mzw.entity.Setmeal;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
