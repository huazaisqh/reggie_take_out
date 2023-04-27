package cn.shuqinghua.Reggie.service.impl;


import cn.shuqinghua.Reggie.common.CustomException;
import cn.shuqinghua.Reggie.mapper.CategoryMapper;
import cn.shuqinghua.Reggie.pojo.Category;
import cn.shuqinghua.Reggie.pojo.Dish;
import cn.shuqinghua.Reggie.pojo.Setmeal;
import cn.shuqinghua.Reggie.service.CategoryService;
import cn.shuqinghua.Reggie.service.DishService;
import cn.shuqinghua.Reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
       //根据id删除分类是否关联了菜品，套餐，如果已经关联了，抛出一个业务异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();

        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if(count1>0){
            //已经关联菜品，抛出一个业务异常
            throw new CustomException("当前分类关联了菜品，不能删除");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if(count2>0){
            throw new CustomException("当前分类关联了套餐，不能删除");
        }
        super.removeById(id);
    }
}
