//package cn.shuqinghua.Reggie.interceptor;
//
//import cn.shuqinghua.Reggie.pojo.Employee;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
///** 拦截器 **/
//@Component
//public class LoginInterceptor implements HandlerInterceptor {
//    /**
//     * 处理的方法（拦截的业务代码写在这里）
//     * @return false 被拦截在资源前， true放行
//     */
//    public  LoginInterceptor(){
//        super();
//        System.out.println("为什么不生效");
//    }
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        HttpSession session = request.getSession();
//        Employee employee = (Employee) session.getAttribute("employee");
//        if (employee==null) {
//            //如果用户没有登录过系统，则重定向到登录页面
//            response.sendRedirect("backend/page/login/login.html");
//            return false;
//        }
//        return true;
//    }
//}
//
