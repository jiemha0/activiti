package com.jiem.activiti.util;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jiem.activiti.domain.Employee;
import com.jiem.activiti.service.IEmployeeService;


/**
 * Ա�������������
 *
 */
@SuppressWarnings("serial")
public class ManagerTaskHandler implements TaskListener {

	@Override
	public void notify(DelegateTask delegateTask) {
		/**�������쳣*/
//		Employee employee = SessionContext.get();
//		//���ø�������İ�����
//		delegateTask.setAssignee(employee.getManager().getName());
		/**���²�ѯ��ǰ�û����ٻ�ȡ��ǰ�û���Ӧ���쵼*/
		Employee employee = SessionContext.getUser();
		//��ǰ�û�
		String name = employee.getName();
		//ʹ�õ�ǰ�û�����ѯ�û�����ϸ��Ϣ
		//��web�л�ȡspring����
		WebApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
		IEmployeeService employeeService = (IEmployeeService) ac.getBean("employeeService");
		Employee emp = employeeService.getEmployeeByName(name);
		//���ø�������İ�����
		delegateTask.setAssignee(emp.getManager().getName());
		
	}

}
