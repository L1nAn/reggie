package com.mzw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzw.entity.Employee;
import com.mzw.mapper.EmployeeMapper;
import com.mzw.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author :马治伟
 * @version :1.0
 * @Date : 2023/4/16
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
