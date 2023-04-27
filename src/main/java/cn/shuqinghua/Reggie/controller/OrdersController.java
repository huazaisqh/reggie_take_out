package cn.shuqinghua.Reggie.controller;

import cn.shuqinghua.Reggie.common.R;
import cn.shuqinghua.Reggie.pojo.OrderDetail;
import cn.shuqinghua.Reggie.pojo.Orders;
import cn.shuqinghua.Reggie.service.OrdersService;
import cn.shuqinghua.Reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;


    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
      ordersService.submit(orders);
      return null;
    }

}
