package com.jiem.activiti.util;

import com.jiem.activiti.domain.Employee;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;


/**
 * ��¼��֤������
 *
 */
@SuppressWarnings("serial")
public class LoginInteceptor implements Interceptor {

	@Override
	public void destroy() {
		

	}

	@Override
	public void init() {
		

	}

	/**ÿ�η���Action��֮ǰ����ִ��intercept����*/
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		//��ȡ��ǰ����Action��URL
		String actionName = invocation.getProxy().getActionName();
		//�����ǰ����Action��URL��"loginAction_login"��ʾ��ʱ��û��Sesion����Ҫ����
		if(!"loginAction_login".equals(actionName)){
			//��Session�л�ȡ��ǰ�û�����
			Employee employee = SessionContext.getUser();
			//���Session�����ڣ���ת����¼ҳ��
			if(employee==null){
				return "login";
			}
		}
		//���У�����Action���з���
		return invocation.invoke();
		
	}

}
