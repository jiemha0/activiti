package com.jiem.activiti.web;

import com.jiem.activiti.domain.Employee;
import com.jiem.activiti.service.IEmployeeService;
import com.jiem.activiti.util.SessionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
public class LoginAction extends ActionSupport implements ModelDriven<Employee> {

	private Employee employee = new Employee();

	@Override
	public Employee getModel() {
		return employee;
	}

	private IEmployeeService employeeService;
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	/**
	 * 登录
	 */
	public String login() {
		String name = employee.getName();
		Employee user = employeeService.getEmployeeByName(name);
		SessionContext.setUser(user);
		return "success";
	}

	
	/**
	 * 标题
	 */
	public String top(){
		return "top";
	}
	
	/**
	 * 主页显示
	 */
	public String welcome() {
		return "welcome";
	}
	
	/**
	 * 左侧菜单
	 */
	public String left() {
		return "left";
	}
	
	
	/**
	 * 退出系统
	 */
	public String logout(){
		SessionContext.setUser(null);
		return "login";
	}
	
}
