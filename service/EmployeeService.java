package com.mhl.service;

import com.mhl.dao.EmployeeDAO;
import com.mhl.domain.Employee;

/*
完成对employee表的各种操作 (通过调用EmployeeDAO对象完成)
 */
public class EmployeeService {
    //定义一个EmployeeDAO属性
    private EmployeeDAO employeeDAO=new EmployeeDAO();

    //方法，根据empId和pwd饭hi一个Employee对象
    //如果查询不到就返回null
    public Employee getEmployeeByIdAndPwd(String empId,String pwd){
        Employee employee = employeeDAO.querysingle("select * from employee where empId=? and pwd=md5(?)", Employee.class, empId, pwd);
    return employee;
    }
}
