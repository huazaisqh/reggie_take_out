package cn.shuqinghua.Reggie.mapper;

import cn.shuqinghua.Reggie.pojo.Orders;
import cn.shuqinghua.Reggie.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
