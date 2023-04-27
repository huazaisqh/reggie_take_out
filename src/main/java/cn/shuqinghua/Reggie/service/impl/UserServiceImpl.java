package cn.shuqinghua.Reggie.service.impl;

import cn.shuqinghua.Reggie.mapper.UserMapper;
import cn.shuqinghua.Reggie.pojo.User;
import cn.shuqinghua.Reggie.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


}
