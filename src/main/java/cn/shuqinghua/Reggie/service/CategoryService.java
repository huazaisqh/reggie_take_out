package cn.shuqinghua.Reggie.service;

import cn.shuqinghua.Reggie.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CategoryService extends IService<Category> {
    /**
     *根据id删除分类，删除之前需要进行判断
     * @param id
     */
    public void remove(Long id);
}
