package cn.shuqinghua.Reggie.service;

import cn.shuqinghua.Reggie.pojo.Orders;
import cn.shuqinghua.Reggie.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrdersService extends IService<Orders> {

     public void submit(Orders orders);
}
