package com.jiem.activiti.service;

import java.util.List;

import com.jiem.activiti.domain.LeaveBill;

public interface ILeaveBillService {

	/**
	 * ��ѯ�Լ�����ٵ�����Ϣ
	 */
	List<LeaveBill> getLeaveBillList();

	/**
	 * ������ٵ�
	 */
	void saveLeaveBill(LeaveBill leaveBill);

	/**
	 * ʹ����ٵ�ID����ѯ��ٵ��Ķ���
	 */
	LeaveBill getLeaveBillById(Long id);

	/**
	 * ʹ����ٵ�ID��ɾ����ٵ�
	 */
	void deleteLeaveBillById(Long id);

}
