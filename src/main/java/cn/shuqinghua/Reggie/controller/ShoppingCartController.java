package cn.shuqinghua.Reggie.controller;

import cn.shuqinghua.Reggie.common.BaseContext;
import cn.shuqinghua.Reggie.common.R;
import cn.shuqinghua.Reggie.pojo.ShoppingCart;
import cn.shuqinghua.Reggie.pojo.User;
import cn.shuqinghua.Reggie.service.ShoppingCartService;
import cn.shuqinghua.Reggie.service.UserService;
import cn.shuqinghua.Reggie.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

   @PostMapping("/add")
     public R<String> add(@RequestBody ShoppingCart shoppingCart,HttpSession session){
       User user = (User) session.getAttribute("user");
       shoppingCart.setUserId(user.getId());
       LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
       queryWrapper.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId());
       queryWrapper.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
       queryWrapper.eq(ShoppingCart::getUserId,user.getId());

       ShoppingCart shoppingCartVo = shoppingCartService.getOne(queryWrapper);
       if(shoppingCartVo==null) {
           shoppingCart.setNumber(1);
           shoppingCartService.save(shoppingCart);
       }
           else {
           shoppingCartVo.setNumber(shoppingCartVo.getNumber()+1);
           shoppingCartService.updateById(shoppingCartVo);
       }
         return R.success("添加成功");
   }


   @RequestMapping("list")
    public R<List<ShoppingCart>> list(){
       Long userId = BaseContext.getCurrentId();
       LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
       queryWrapper.eq(ShoppingCart::getUserId,userId);
       List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
       return R.success(list);
    }

    /**
     * 购物车减数量
     * @return
     */
    @RequestMapping("sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart){
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId());
        queryWrapper.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        queryWrapper.eq(ShoppingCart::getUserId,userId);


        ShoppingCart shoppingCartVo = shoppingCartService.getOne(queryWrapper);
      if(shoppingCartVo.getNumber()==1){
          shoppingCartService.removeById(shoppingCartVo);
      }else {
          shoppingCartVo.setNumber(shoppingCartVo.getNumber()-1);
          shoppingCartService.updateById(shoppingCartVo);
      }
        return R.success("减少成功");
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
       shoppingCartService.clean();
        return R.success("清空成功");
    }
}
