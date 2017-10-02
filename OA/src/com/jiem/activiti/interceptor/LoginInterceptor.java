package com.jiem.activiti.interceptor;

import com.jiem.activiti.domain.Employee;
import com.jiem.activiti.util.SessionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * µÇÂ¼ÑéÖ¤À¹½ØÆ÷
 * @author Administrator
 *
 */

@SuppressWarnings("serial")
public class LoginInterceptor implements Interceptor {

	@Override
	public void destroy() {

	}

	@Override
	public void init() {

	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		
		String actionName = invocation.getProxy().getActionName();
		if(!"loginAction_login".equals(actionName)){
			Employee user = SessionContext.getUser();
			if(user == null ){
				return "login";
			}
		}
		return invocation.invoke();
	}

}
