package com.jiem.activiti.dao;

import com.jiem.activiti.domain.Employee;

public interface IEmployeeDao {
	Employee getEmployeeByName(String name);
}
