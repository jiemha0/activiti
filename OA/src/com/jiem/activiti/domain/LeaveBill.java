package com.jiem.activiti.domain;

import java.util.Date;

/**
 * ��ٵ�
 */
public class LeaveBill {
	
	private Long id;//����ID
	private Integer days;// �������
	private String content;// �������
	private Date leaveDate = new Date();// ���ʱ��
	private String remark;// ��ע
	private Employee user;// �����
	
	private Integer state=0;// ��ٵ�״̬ 0��ʼ¼��,1.��ʼ����,2Ϊ�������

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Employee getUser() {
		return user;
	}

	public void setUser(Employee user) {
		this.user = user;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	
}
