//package cn.shuqinghua.Reggie.config;
//
//import cn.shuqinghua.Reggie.interceptor.LoginInterceptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.ArrayList;
//import java.util.List;
//
///** 拦截器注册 **/
//@Configuration
//public class LoginInterceptorConfigure implements WebMvcConfigurer {
//
//    @Autowired
//    private LoginInterceptor loginInterceptor;
//    /**
//     * 配置拦截器对象
//     * @param registry
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        //放行的请求（白名单）
//        List<String> whiteList = new ArrayList<>();
//        whiteList.add("/backend/**");
//        whiteList.add("/front/**");
////        whiteList.add("/user/hello");
////        whiteList.add("/css/**");
////        whiteList.add("/js/**");
////        whiteList.add("/images/**");
////        whiteList.add("/bootstrap3/**");
////        whiteList.add("/web/login.html");
////        whiteList.add("/user/hello");
////        whiteList.add("/user/reg");
////        whiteList.add("/user/login");
//        //addPathPatterns：添加拦截路径
//
//            registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(whiteList);
//        WebMvcConfigurer.super.addInterceptors(registry);
//    }
// }
