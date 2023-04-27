package cn.shuqinghua.Reggie.controller;

import cn.shuqinghua.Reggie.common.R;
import cn.shuqinghua.Reggie.pojo.Category;
import cn.shuqinghua.Reggie.pojo.Dish;
import cn.shuqinghua.Reggie.dto.DishDto;
import cn.shuqinghua.Reggie.pojo.DishFlavor;
import cn.shuqinghua.Reggie.service.CategoryService;
import cn.shuqinghua.Reggie.service.DishFlavorService;
import cn.shuqinghua.Reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;


    /**
     * 添加菜品和对应的口味
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> saveWithFlavor(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
         return R.success("新增菜品成功");
    }


    /**
     * 分页菜品查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public R<Page<DishDto>> queryByPage(Integer page,Integer pageSize,String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
        //分页构造器
        Page<Dish> pageInfo = new Page(page, pageSize);

        Page<DishDto> dishDtoInfo = new Page<>();
        //添加过滤条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getIsDeleted,0);
        //like中自动判断是否为空
        queryWrapper.like(StringUtils.isNotBlank(name),Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, queryWrapper);

      //对象copy
        BeanUtils.copyProperties(pageInfo,dishDtoInfo,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> dishDtos = records.stream().map(item -> {
            DishDto dishDto = new DishDto();
            Category category = categoryService.getById(item.getCategoryId());
            if(null!=category.getName()){
                dishDto.setCategoryName(category.getName());
            }
            //copy properties
            BeanUtils.copyProperties(item, dishDto);
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoInfo.setRecords(dishDtos);

        return R.success(dishDtoInfo);
    }

    /**
     * 根据id查找菜品dto 回显数据
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public R<DishDto> queryByIdWithFlavor(@PathVariable("id")Long id){
        DishDto dishDto=dishService.queryByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     */
    @PutMapping
    public R<String> updateWithFlavor(@RequestBody DishDto dishDto ){
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功");
    }

    /**
     * 根据status停售
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> editStatusById(@PathVariable("status")Integer status,Long ids){

        dishService.editStatusById(status,ids);
        return R.success("出售状态改变成功");
    }

    @DeleteMapping
    public R<String> deleteByIdWithFlavor(Long[] ids){
        for (Long id : ids) {
            dishService.deleteByIdWithFlavor(id);
        }
         return R.success("删除成功");
    }

//    @GetMapping("/list")
//    public R<List<Dish>> queryListByCategoryId(Dish dish)
//    {
//        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper();
//        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId ,dish.getCategoryId());
//        queryWrapper.eq(dish.getStatus()!=null,Dish::getStatus,dish.getStatus());
//        queryWrapper.eq(Dish::getStatus,1);
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> list = dishService.list(queryWrapper);
//        return R.success(list);
//    }


    @GetMapping("/list")
    public R<List<DishDto>> queryListByCategoryId(Dish dish)
    {
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId ,dish.getCategoryId());
        queryWrapper.eq(dish.getStatus()!=null,Dish::getStatus,dish.getStatus());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishes = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = dishes.stream().map(dish1 -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish1, dishDto);

            Category category = categoryService.getById(dish1.getCategoryId());
            if(null!=category.getName()){
                dishDto.setCategoryName(category.getName());
            }
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, dish1.getId());
            List<DishFlavor> flavors = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(flavors);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtoList);
    }

}
