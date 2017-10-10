package com.jiem.activiti.dao;

import java.util.List;

import com.jiem.activiti.domain.LeaveBill;

public interface ILeaveBillDao {

	/**
	 * 查询自己的请假单的信息
	 */
	List<LeaveBill> getLeaveBillList();

	/**
	 * 保存请假单
	 */
	void saveLeaveBill(LeaveBill leaveBill);

	/**
	 * 通过请假单ID，查询请假单的对象
	 */
	LeaveBill getLeaveBillById(Long id);

	/**
	 * 更新请假单
	 */
	void updateLeaveBill(LeaveBill leaveBill);

	/**
	 * 通过请假单ID，删除请假单
	 */
	void deleteLeaveBillById(Long id);


}
