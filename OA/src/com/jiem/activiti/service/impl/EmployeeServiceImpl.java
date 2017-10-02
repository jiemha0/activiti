package com.jiem.activiti.service.impl;

import com.jiem.activiti.dao.IEmployeeDao;
import com.jiem.activiti.domain.Employee;
import com.jiem.activiti.service.IEmployeeService;

public class EmployeeServiceImpl implements IEmployeeService{

	private IEmployeeDao employeeDao;
	public void setEmployeeDao(IEmployeeDao employeeDao) {
		this.employeeDao = employeeDao;
	}
	
	@Override
	public Employee getEmployeeByName(String name) {
		Employee user = employeeDao.getEmployeeByName(name);
		return user;
	}

}
