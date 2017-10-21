package com.jiem.activiti.dao;

import java.util.List;

import com.jiem.activiti.domain.LeaveBill;

public interface ILeaveBillDao {

	/**
	 * ��ѯ�Լ�����ٵ�����Ϣ
	 */
	List<LeaveBill> getLeaveBillList();

	/**
	 * ������ٵ�
	 */
	void saveLeaveBill(LeaveBill leaveBill);

	/**
	 * ͨ����ٵ�ID����ѯ��ٵ��Ķ���
	 */
	LeaveBill getLeaveBillById(Long id);

	/**
	 * ������ٵ�
	 */
	void updateLeaveBill(LeaveBill leaveBill);

	/**
	 * ͨ����ٵ�ID��ɾ����ٵ�
	 */
	void deleteLeaveBillById(Long id);


}
