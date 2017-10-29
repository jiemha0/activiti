package com.jiem.activiti.task;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

public class TaskTest {

	private ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	
	//@Test //执行流程
	public void startProcess(){
		String processDefinationKey="buyBill"; 
		
		ProcessInstance pi = processEngine.getRuntimeService()
				.startProcessInstanceByKey(processDefinationKey);
		
		System.out.println("流程执行对象的id："+pi.getId());//Execution 对象
		System.out.println("流程实例的id："+pi.getProcessInstanceId());//ProcessInstance 对象
		System.out.println("流程定义的id："+pi.getProcessDefinitionId());//默认执行的是最新版本的流程定义	
	}
	
	
	@Test  //查询正在运行任务 
	public void queryTask() {
		TaskService taskService = processEngine.getTaskService();
		TaskQuery taskQuery = taskService.createTaskQuery();
		
		List<Task> list = taskQuery.list();
		if(list!=null&&list.size()>0){
			for (Task task : list) {
				System.out.print("任务的id："+task.getId());
				System.out.print(" 任务的办理人："+task.getAssignee());
				System.out.println(" 任务的名称："+task.getName());
			}
		}
	}
	
	@Test  //完成任务
	public void compileTask() {
		String taskId = "1102";
		
		processEngine.getTaskService().complete(taskId);
		System.out.println("当前任务执行完毕");
	}
	
	
	@Test  //获取流程实例的状态
	public void getProcessInstanceState(){
		String processInstanceId="901";
		
		ProcessInstance singleResult = processEngine.getRuntimeService()
				.createProcessInstanceQuery()
				.processInstanceId(processInstanceId)
				.singleResult();
		
		if(singleResult != null){
			 System.out.println("该流程实例"+processInstanceId+"正在运行...  "+"当前活动的任务:"+singleResult.getActivityId());
		 }else{
			 System.out.println("当前的流程实例"+processInstanceId+" 已经结束！");
		 }
		
	}
	
	@Test  //查看历史执行流程实例信息
	public void queryHistoryProcInst(){
		List<HistoricProcessInstance> list = processEngine.getHistoryService()
				.createHistoricProcessInstanceQuery().list();
		if (list != null && list.size() > 0) {
			for (HistoricProcessInstance temp : list) {
				System.out.println("历史流程实例id:" + temp.getId());
				System.out.println("历史流程定义的id:" + temp.getProcessDefinitionId());
				System.out.println("历史流程实例开始时间--结束时间:" + temp.getStartTime() + "-->" + temp.getEndTime());
			}
		}
	}
	
	@Test //查看历史执行流程任务信息
	public void queryHistoryTask(){
		String processInstanceId="901";
		
		List<HistoricTaskInstance> list = processEngine.getHistoryService()
				.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceId)
				.list();
		if (list != null && list.size() > 0) {
			for (HistoricTaskInstance temp : list) {
				System.out.print(" 历史流程实例任务id:"+temp.getId());
				System.out.print(" 历史流程定义的id:"+temp.getProcessDefinitionId());
				System.out.print(" 历史流程实例任务名称:"+temp.getName());
				System.out.println(" 历史流程实例任务处理人:"+temp.getAssignee());
			}
		}
	}
	
	
	
}
