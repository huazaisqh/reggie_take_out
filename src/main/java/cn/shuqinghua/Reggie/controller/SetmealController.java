package cn.shuqinghua.Reggie.controller;

import cn.shuqinghua.Reggie.common.R;
import cn.shuqinghua.Reggie.dto.DishDto;
import cn.shuqinghua.Reggie.dto.SetmealDto;
import cn.shuqinghua.Reggie.pojo.Category;
import cn.shuqinghua.Reggie.pojo.Dish;
import cn.shuqinghua.Reggie.pojo.Setmeal;
import cn.shuqinghua.Reggie.pojo.SetmealDish;
import cn.shuqinghua.Reggie.service.CategoryService;
import cn.shuqinghua.Reggie.service.DishService;
import cn.shuqinghua.Reggie.service.SetmealDishService;
import cn.shuqinghua.Reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 分页菜品查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public R<Page<SetmealDto>> queryByPage(Integer page, Integer pageSize, String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
        //分页构造器
        Page<Setmeal> pageInfo = new Page(page, pageSize);

        Page<SetmealDto> setmealDtoInfo = new Page<>();
        //添加过滤条件
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //like中自动判断是否为空
        queryWrapper.like(StringUtils.isNotBlank(name),Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, queryWrapper);

        //对象copy 排除了records
        BeanUtils.copyProperties(pageInfo,setmealDtoInfo,"records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> setmealDtos = records.stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            Category category = categoryService.getById(item.getCategoryId());
            if(null!=category.getName()){
                setmealDto.setCategoryName(category.getName());
            }
            //copy properties
            BeanUtils.copyProperties(item, setmealDto);
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoInfo.setRecords(setmealDtos);

        return R.success(setmealDtoInfo);
    }


    /**
     * 添加套餐和对应的菜品
     * @param SetmealDto
     * @return
     */
    @PostMapping
    public R<String> saveWithDish(@RequestBody SetmealDto SetmealDto){
        setmealService.saveWithDish(SetmealDto);
        return R.success("新增套餐成功");
    }


    /**
     * 根据id查找菜单dto 回显数据
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public R<SetmealDto> queryByIdWithDish(@PathVariable("id")Long id){
        SetmealDto setmealDto=setmealService.queryByIdWithFlavor(id);
        return R.success(setmealDto);
    }


    /**
     * 修改套餐
     */
    @PutMapping
    public R<String> updateWithFlavor(@RequestBody SetmealDto setmealDto  ){
        setmealService.updateWithFlavor(setmealDto);
        return R.success("修改成功");
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){

        setmealService.removeWhithDish(ids);
        return R.success("删除成功");
    }

    @GetMapping("/dish/{id}")
    public R<Dish> queryById(@PathVariable Long id){
        Dish dish = dishService.getById(id);
        return R.success(dish);
    }


    @GetMapping("/list")
    public R<List<SetmealDto>> list(Setmeal setmeal){
         LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
         queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
         queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());

        List<Setmeal> setmeals = setmealService.list(queryWrapper);

        List<SetmealDto> setmealDtoList = setmeals.stream().map(setmeal1 -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal1, setmealDto);
            LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmeal1.getId());
            List<SetmealDish> setmealDishes = setmealDishService.list(lambdaQueryWrapper);
            setmealDto.setSetmealDishes(setmealDishes);
            return setmealDto;
        }).collect(Collectors.toList());
        return R.success(setmealDtoList);
    }
}
