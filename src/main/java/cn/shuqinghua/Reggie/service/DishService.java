package cn.shuqinghua.Reggie.service;

import cn.shuqinghua.Reggie.dto.DishDto;
import cn.shuqinghua.Reggie.pojo.Dish;
import cn.shuqinghua.Reggie.pojo.Employee;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);

    DishDto queryByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);

    void editStatusById(Integer status, Long id);

    void deleteByIdWithFlavor(Long id);
}
