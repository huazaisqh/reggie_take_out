package cn.shuqinghua.Reggie.controller;

import cn.shuqinghua.Reggie.common.R;
import cn.shuqinghua.Reggie.pojo.Category;
import cn.shuqinghua.Reggie.pojo.Employee;
import cn.shuqinghua.Reggie.service.CategoryService;
import cn.shuqinghua.Reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;



    /**
     * 添加种类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Category category){
         categoryService.save(category);
         return R.success("添加成功");
    }


    /**
     * 分页查询种类
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public R<Page<Category>> queryByPage(Integer page, Integer pageSize){
        log.info("page={},pageSize={}",page,pageSize);
        //分页构造器
        Page<Category> pageInfo = new Page(page, pageSize);
        //添加过滤条件
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //like中自动判断是否为空
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }


    /**
     * 修改分类
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody  Category category ){
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    /**
     * 删除分类
     * @param ids
     * @return
     */
    @RequestMapping
    public R<String> deleteById(Long ids){
//       categoryService.removeById(ids);
        categoryService.remove(ids);
        return R.success("删除成功");
    }


    /**
     * 根据菜品type查询
     * @param category
     * @return
     */
    @GetMapping("list")
    public R<List<Category>> list(Category category){

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //根据type查询
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> categories = categoryService.list(queryWrapper);
        return R.success(categories);
    }
}
