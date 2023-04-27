package cn.shuqinghua.Reggie.config;

import cn.shuqinghua.Reggie.filter.LoginFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

@Configuration
public class LoginFilterConfig implements WebMvcConfigurer {



    /**
     * 添加登录过滤器
     */
    @Bean
    public FilterRegistrationBean<Filter> loginFilterRegistration() {
        // 注册LoginFilter
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LoginFilter());
        // 设置名称
        registrationBean.setName("loginFilter");
        // 设置拦截路径

        return registrationBean;
    }

}
