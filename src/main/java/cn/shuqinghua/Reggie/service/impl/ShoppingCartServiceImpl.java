package cn.shuqinghua.Reggie.service.impl;

import cn.shuqinghua.Reggie.common.BaseContext;
import cn.shuqinghua.Reggie.mapper.ShoppingCartMapper;
import cn.shuqinghua.Reggie.pojo.ShoppingCart;
import cn.shuqinghua.Reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {


    @Override
    public void clean() {
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        this.remove(queryWrapper);
    }
}
