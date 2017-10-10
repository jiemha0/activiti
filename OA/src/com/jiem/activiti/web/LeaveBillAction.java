package com.jiem.activiti.web;

import java.util.List;

import com.jiem.activiti.domain.LeaveBill;
import com.jiem.activiti.service.ILeaveBillService;
import com.jiem.activiti.util.ValueContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
public class LeaveBillAction  extends ActionSupport implements ModelDriven<LeaveBill> {

	private LeaveBill leaveBill = new LeaveBill();
	@Override
	public LeaveBill getModel() {
		return leaveBill;
	}

	private ILeaveBillService leaveBillService;
	public void setLeaveBillService(ILeaveBillService leaveBillService) {
		this.leaveBillService = leaveBillService;
	}
	
	
	/**
	 * «ÎºŸπ‹¿Ì ◊“≥œ‘ æ
	 */
	public String home(){
		List<LeaveBill> leaveBillList = leaveBillService.getLeaveBillList();
		ValueContext.putValueContext("list", leaveBillList);
		return "home";
	}
	
	/**
	 * ÃÌº”«ÎºŸ…Í«Î
	 */
	public String input(){
		Long id = leaveBill.getId();
		if(id != null){
			LeaveBill bill = leaveBillService.getLeaveBillById(id);
			ValueContext.putValueStack(bill);
		}
		return "input";
	}
	
	/**
	 * ±£¥Ê/∏¸–¬£¨«ÎºŸ…Í«Î
	 */
	public String save() {
		leaveBillService.saveLeaveBill(leaveBill);
		return "save";
	}
	
	/**
	 * …æ≥˝£¨«ÎºŸ…Í«Î
	 */
	public String delete(){
		Long id = leaveBill.getId();
		leaveBillService.deleteLeaveBillById(id);
		return "save";
	}
	
}
