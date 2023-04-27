package cn.shuqinghua.Reggie.controller;

import cn.shuqinghua.Reggie.common.R;
import cn.shuqinghua.Reggie.pojo.User;
import cn.shuqinghua.Reggie.service.UserService;
import cn.shuqinghua.Reggie.utils.SMSUtils;
import cn.shuqinghua.Reggie.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

   @PostMapping("/sendMsg")
     public R<String> sendMsg(@RequestBody User user, HttpSession session){
       String phone = user.getPhone();
      if(StringUtils.isNotBlank(phone)){

          String code = ValidateCodeUtils.generateValidateCode(6).toString();
//          SMSUtils.sendMessage("shuqinghua","",phone,code)
//       SMSUtils.sendMessage;
           log.info("手机验证码：{}",code);
            session.setAttribute(phone,code);
          return R.success("手机验证码短信发送成功");
      }
       return R.error("手机验证码短信发送失败");
   }


   @PostMapping("/login")
   public R<User> login(@RequestBody Map map,HttpSession session){
       //
       String phone = (String) map.get("phone");
       String code = (String) map.get("code");

       Object sessionCode = session.getAttribute(phone);
       if(sessionCode!=null&&sessionCode.equals(code)){
           //判断是否是新用户
           LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
           queryWrapper.eq(User::getPhone,phone);
           User user = userService.getOne(queryWrapper);
           if(user==null){
               user=new User();
               user.setPhone(phone);
               user.setStatus(1);
               userService.save(user);
           }
           session.setAttribute("user",user);
           return R.success(user);
       }
       return R.error("登录失败");
   }

}
