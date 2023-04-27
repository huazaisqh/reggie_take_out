package cn.shuqinghua.Reggie.service;

import cn.shuqinghua.Reggie.pojo.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ShoppingCartService extends IService<ShoppingCart> {
    void clean();
}
