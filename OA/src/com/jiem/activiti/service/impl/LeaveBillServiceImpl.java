package com.jiem.activiti.service.impl;

import java.util.List;

import com.jiem.activiti.dao.ILeaveBillDao;
import com.jiem.activiti.domain.LeaveBill;
import com.jiem.activiti.service.ILeaveBillService;

public class LeaveBillServiceImpl implements ILeaveBillService {

	private ILeaveBillDao leaveBillDao;
	public void setLeaveBillDao(ILeaveBillDao leaveBillDao) {
		this.leaveBillDao = leaveBillDao;
	}
	
	@Override
	public List<LeaveBill> getLeaveBillList() {
		List<LeaveBill> leaveBillList = leaveBillDao.getLeaveBillList();
		return leaveBillList;
	}

	@Override
	public void saveLeaveBill(LeaveBill leaveBill) {
		leaveBillDao.saveLeaveBill(leaveBill);		
	}

	@Override
	public LeaveBill getLeaveBillById(Long id) {
		LeaveBill leaveBillById = leaveBillDao.getLeaveBillById(id);
		return leaveBillById;
	}

	@Override
	public void deleteLeaveBillById(Long id) {
		leaveBillDao.deleteLeaveBillById(id);
	}

}
