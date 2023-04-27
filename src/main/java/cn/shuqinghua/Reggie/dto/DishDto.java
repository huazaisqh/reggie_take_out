package cn.shuqinghua.Reggie.dto;

import cn.shuqinghua.Reggie.pojo.Dish;
import cn.shuqinghua.Reggie.pojo.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
