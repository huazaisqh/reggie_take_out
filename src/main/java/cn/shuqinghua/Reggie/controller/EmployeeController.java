package cn.shuqinghua.Reggie.controller;

import cn.shuqinghua.Reggie.common.R;
import cn.shuqinghua.Reggie.pojo.Employee;
import cn.shuqinghua.Reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        if(StringUtils.isBlank(password)){
            return R.error("未输入密码");
        }
        password =DigestUtils.md5DigestAsHex(password.getBytes());
        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3、如果没有查询到则返回登录失败结果
         if(emp==null){
             return R.error("没有查找到用户");
         }
        //4、密码比对，如果不一致则返回登录失败结果
          if(!emp.getPassword().equals(password)){
               return R.error("密码错误");
          }
        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if(emp.getStatus()==0){
            return R.error("账号已禁用");
        }
        //6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp);
        return R.success(emp);
    }


    //logout
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
         return R.success("退出成功");
    }

    /**
     * 添加员工
     * @param employee
     * @return
     */
    @PostMapping()
    public R<String> addEmp(HttpServletRequest request,@RequestBody Employee employee){
         employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//         employee.setCreateTime(LocalDateTime.now());
//         employee.setUpdateTime(LocalDateTime.now());

//        Employee useEmp = (Employee) request.getSession().getAttribute("employee");
//        employee.setCreateUser(useEmp.getId());
//        employee.setUpdateUser(useEmp.getId());
        employee.setStatus(1);
         employeeService.save(employee);
         return R.success("添加成功");
    }


    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public R<Page<Employee>> queryByPage(Integer page,Integer pageSize,String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
        //分页构造器
        Page<Employee> pageInfo = new Page(page, pageSize);
        //添加过滤条件
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //like中自动判断是否为空
        queryWrapper.like(StringUtils.isNotBlank(name),Employee::getName,name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }


    /**
     * 修改禁用状态  跟新员工数据
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody  Employee employee){
//        employeeService.updateById(employee);
//        Employee  emp  = (Employee) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(emp.getId());
        employeeService.updateById(employee);
        return R.success("修改成功");
    }

    /**
     * 根据id查找员工并回显
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public R<Employee> queryById(@PathVariable("id")Long id){
        Employee emp = employeeService.getById(id);
        return R.success(emp);
    }
}
