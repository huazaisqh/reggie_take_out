package cn.shuqinghua.Reggie.service.impl;


import cn.shuqinghua.Reggie.common.CustomException;
import cn.shuqinghua.Reggie.dto.DishDto;
import cn.shuqinghua.Reggie.mapper.DishMapper;
import cn.shuqinghua.Reggie.pojo.Dish;
import cn.shuqinghua.Reggie.pojo.DishFlavor;
import cn.shuqinghua.Reggie.service.DishFlavorService;
import cn.shuqinghua.Reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        //保存菜品口味表 dish_flavor
        dishDto.getFlavors().forEach(dishFlavor -> {
                 dishFlavor.setDishId(dishDto.getId());
                 dishFlavorService.save(dishFlavor);
        });
    }


    @Override
    public DishDto queryByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);

        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        if(!CollectionUtils.isEmpty(list)){
            dishDto.setFlavors(list);
        }
        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        dishDto.getFlavors().stream().forEach(dishFlavor -> {
            dishFlavor.setDishId(dishDto.getId());
            dishFlavorService.save(dishFlavor);
        });
    }

    @Override
    public void editStatusById(Integer status, Long id) {
        Dish dish = this.getById(id);
        dish.setStatus(status);
        this.updateById(dish);
    }

    @Override
    public void deleteByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        //判断是否在售 在售抛一个异常
        if(dish.getStatus()==1){
         throw new CustomException(dish.getName()+"商品在售中不能删除");
        }
        dish.setIsDeleted(1);
        this.updateById(dish);
    }
}
