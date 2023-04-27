package cn.shuqinghua.Reggie.filter;

import cn.shuqinghua.Reggie.common.BaseContext;
import cn.shuqinghua.Reggie.common.R;
import cn.shuqinghua.Reggie.pojo.Employee;
import cn.shuqinghua.Reggie.pojo.User;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



@Slf4j
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
public class LoginFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        HttpServletResponse response=(HttpServletResponse)servletResponse;
        //获取请求uri
        String requestURI = request.getRequestURI();
        log.info("拦截到请求：{}",requestURI);
        //定义不需要处理请求路径
        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
                "/favicon.ico"
        };
        boolean check = check(urls, requestURI);
        if(check){
            log.info("本次请求不用处理:{}",requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        Employee employee = (Employee) request.getSession().getAttribute("employee");
        //判断登录状态
        if(employee!=null){
            //给当前线程localThread中赋值
            BaseContext.setCurrentId(employee.getId());
            log.info("员工已登录，用户ID为：{}",request.getSession().getAttribute("employee"));
            filterChain.doFilter(request, response);
            return;
        }
        User user = (User) request.getSession().getAttribute("user");
        //判断登录状态
        if(user!=null){
            //给当前线程localThread中赋值
            BaseContext.setCurrentId(user.getId());
            log.info("用户已登录，用户ID为：{}",request.getSession().getAttribute("user"));
            filterChain.doFilter(request, response);
            return;
        }
        //还未登录通过输出流，返回NOTLOGIN信息，前端响应拦截器进行页面跳转
        log.info("用户未登录！");
       response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配检查此次请求是否放行
     * @param urls
     * @param requestURI
     * @return
     */
    public static boolean check(String[] urls, String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
