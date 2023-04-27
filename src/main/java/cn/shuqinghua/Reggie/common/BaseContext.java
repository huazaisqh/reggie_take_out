package cn.shuqinghua.Reggie.common;

import cn.shuqinghua.Reggie.pojo.Employee;

public class BaseContext {
    private static ThreadLocal<Long> THREADLOCAL=new ThreadLocal<>();

    public static void setCurrentId(Long id){
        THREADLOCAL.set(id);
    }

    public static Long getCurrentId(){
      return   THREADLOCAL.get();
    }
}
