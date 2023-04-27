package cn.shuqinghua.Reggie.service.impl;


import cn.shuqinghua.Reggie.mapper.EmployeeMapper;
import cn.shuqinghua.Reggie.pojo.Employee;
import cn.shuqinghua.Reggie.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService {

}
