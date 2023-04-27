package cn.shuqinghua.Reggie.dto;

import cn.shuqinghua.Reggie.pojo.Setmeal;
import cn.shuqinghua.Reggie.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
