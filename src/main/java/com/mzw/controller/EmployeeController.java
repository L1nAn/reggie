package com.mzw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mzw.common.R;
import com.mzw.entity.Employee;
import com.mzw.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.logging.Handler;

/**
 * @author :马治伟
 * @version :1.0
 * @Date : 2023/4/16
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //1、将页面提交的密码进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2、根据页面提交的用户名username查询数据库
//        String name = employee.getName();
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //eq是等值查询，like是模糊查询
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);//因为数据库中name字段是唯一性，所以用的是getOne()方法

        //3、如果没有查询到则返回登陆失败结果
        if(emp == null) {
            return R.error("登陆失败");
        }
        //4、密码比对，如果不一致则返回登陆失败结果
        if(!emp.getPassword().equals(password)) {
            return R.error("登录失败");
        }
        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if(emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }
        //6、登陆成功，将员工id存入Session并返回登陆成功结果。
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //清理之前删除session中的员工id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping//不用加路径
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {//RequestBody 因为是JSON数据
        log.info("新增员工，员工信息:{}", employee.toString());
        //设置初始密码123456，需要进行MD5加密处理，后期员工可以自己登陆修改密码，status不用设置，因为数据库再保存时有默认值
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //创建时间
//        employee.setCreateTime(LocalDateTime.now());
//        //修改时间
//        employee.setUpdateTime(LocalDateTime.now());
//
//        //获取当前登录用户的id
//        Long empId = (long) request.getSession().getAttribute("employee");
//        //创建用户，谁创建的？把id保存起来。
//        employee.setCreateUser(empId);
//        //修改用户，就是管理员？？？
//        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("新增员工成功");
    }

    /**
     * 分页功能，根据条件
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
//        log.info("Page = " + page + "pageSize = " + pageSize);
        //这种写法记得学一下，占位符
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper();
        //添加过滤条件,课程视频中所讲的方法已经被淘汰了
        lqw.like(Strings.isNotEmpty(name), Employee::getName, name);//当name不等于null时才会添加
        //添加排序条件, 根据更新时间
        lqw.orderByDesc(Employee::getUpdateTime);
        //执行查询,,,这并不需要返回，因为数据都已经被封装在pageInfo这个对象里面了。可以点击进去看一下。
        employeeService.page(pageInfo, lqw);

        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息,这是通用的。  修改员工状态和修改员工信息是一样的，只不过修改员工信息有回显功能。
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(employee.toString());

        long id = Thread.currentThread().getId();
        log.info("线程id为：{}",id);

//        Long empId = (long) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("根据id查询员工信息");
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");
    }
}
