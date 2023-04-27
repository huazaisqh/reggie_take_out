package cn.shuqinghua.Reggie.service.impl;


import cn.shuqinghua.Reggie.common.CustomException;
import cn.shuqinghua.Reggie.dto.SetmealDto;
import cn.shuqinghua.Reggie.mapper.SetmealMapper;
import cn.shuqinghua.Reggie.pojo.*;
import cn.shuqinghua.Reggie.service.SetmealDishService;
import cn.shuqinghua.Reggie.service.SetmealService;
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
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

      @Autowired
      private SetmealDishService setmealDishService;

      @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息到套餐表setmeal
        this.save(setmealDto);

        //保存菜品口味表 setmealDish
        setmealDto.getSetmealDishes().stream().forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealDto.getId());
            setmealDishService.save(setmealDish);
        });
    }

    @Override
    public SetmealDto queryByIdWithFlavor(Long id) {

        Setmeal setmeal = this.getById(id);
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getSetmealId,id);
            List<SetmealDish> list = setmealDishService.list(queryWrapper);

            SetmealDto setmealDto=new SetmealDto();
            BeanUtils.copyProperties(setmeal,setmealDto);
            if(!CollectionUtils.isEmpty(list)){
                setmealDto.setSetmealDishes(list);
            }
            return setmealDto;
    }

    @Override
    public void updateWithFlavor(SetmealDto setmealDto) {

          this.updateById(setmealDto);
            LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
            setmealDishService.remove(queryWrapper);
            setmealDto.getSetmealDishes().stream().forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealDto.getId());
                setmealDishService.save(setmealDish);
            });
      }

    @Override
    public void removeWhithDish(List<Long> ids) {
         String sql="select * from setmeal where id in () and status=1";
          //查询套餐状态 确定是否可删除
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = this.count(queryWrapper);

        //如果不能删除，抛出一个业务异常
        if(count>0){
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        //如果可以删除，先删除表中的数据
        this.removeByIds(ids);

        //删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);

        setmealDishService.remove(lambdaQueryWrapper);




    }
}