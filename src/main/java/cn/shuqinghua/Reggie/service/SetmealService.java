package cn.shuqinghua.Reggie.service;

import cn.shuqinghua.Reggie.dto.SetmealDto;
import cn.shuqinghua.Reggie.pojo.Employee;
import cn.shuqinghua.Reggie.pojo.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);

    SetmealDto queryByIdWithFlavor(Long id);

    void updateWithFlavor(SetmealDto setmealDto);

    void removeWhithDish(List<Long> ids);
}
