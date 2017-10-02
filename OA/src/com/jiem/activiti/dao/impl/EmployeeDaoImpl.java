package com.jiem.activiti.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.jiem.activiti.dao.IEmployeeDao;
import com.jiem.activiti.domain.Employee;

public class EmployeeDaoImpl  extends HibernateDaoSupport implements IEmployeeDao {

	@Override
	public Employee getEmployeeByName(String name) {
		String hql = "from Employee e where e.name = ?";
		List<Employee> list = this.getHibernateTemplate().find(hql,name);
		Employee employee = null ;
		if(list != null && list.size() > 0){
			employee = list.get(0);
		}
		return employee;
	}

}
