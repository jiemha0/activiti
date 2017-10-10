package com.jiem.activiti.service;

import java.util.List;

import com.jiem.activiti.domain.LeaveBill;

public interface ILeaveBillService {

	/**
	 * 查询自己的请假单的信息
	 */
	List<LeaveBill> getLeaveBillList();

	/**
	 * 保存请假单
	 */
	void saveLeaveBill(LeaveBill leaveBill);

	/**
	 * 使用请假单ID，查询请假单的对象
	 */
	LeaveBill getLeaveBillById(Long id);

	/**
	 * 使用请假单ID，删除请假单
	 */
	void deleteLeaveBillById(Long id);

}
