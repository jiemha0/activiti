package com.jiem.activiti.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.jiem.activiti.dao.ILeaveBillDao;
import com.jiem.activiti.domain.Employee;
import com.jiem.activiti.domain.LeaveBill;
import com.jiem.activiti.util.SessionContext;

public class LeaveBillDaoImpl  extends HibernateDaoSupport implements ILeaveBillDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<LeaveBill> getLeaveBillList() {
		Employee user = SessionContext.getUser();
		String hql = "from LeaveBill l where l.user = ?";
		List<LeaveBill> list = this.getHibernateTemplate().find(hql,user);
		return list;
	}

	@Override
	public void saveLeaveBill(LeaveBill leaveBill) {
		this.getHibernateTemplate().save(leaveBill);
	}

	@Override
	public LeaveBill getLeaveBillById(Long id) {
		return this.getHibernateTemplate().get(LeaveBill.class, id);
	}

	@Override
	public void updateLeaveBill(LeaveBill leaveBill) {
		this.getHibernateTemplate().update(leaveBill);
	}

	@Override
	public void deleteLeaveBillById(Long id) {
		LeaveBill leaveBill = this.getLeaveBillById(id);
		this.getHibernateTemplate().delete(leaveBill);
	}

}
