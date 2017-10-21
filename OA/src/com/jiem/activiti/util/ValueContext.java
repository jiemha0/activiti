package com.jiem.activiti.util;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

public class ValueContext {

	/**
	 * ������Rootջ��
	 * @param key
	 * @param value
	 */
	public static void putValueContext(String key,Object value){
		ActionContext.getContext().put(key, value);
	}
	
	/**
	 * ѹ��ջ��
	 * @param o
	 */
	public static void putValueStack(Object o){
		ServletActionContext.getContext().getValueStack().push(o);
	}
}