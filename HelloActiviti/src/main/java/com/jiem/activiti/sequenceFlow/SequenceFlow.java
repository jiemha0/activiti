package com.jiem.activiti.sequenceFlow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

//连线
public class SequenceFlow {

	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	@Test  // 部署流程定义
	public void deployProcessDefi() {
		Deployment deploy = processEngine.getRepositoryService()
				.createDeployment().name("连线流程")
				.addClasspathResource("diagrams/SequenceFlow.bpmn")
				.deploy();

		System.out.println("部署名称:" + deploy.getName());
		System.out.println("部署id:" + deploy.getId());
	}
	
	@Test  // 执行流程，开始跑流程
	public void startProcess() {
		String processDefiKey = "sequenceBill";
		ProcessInstance pi = processEngine.getRuntimeService()
				.startProcessInstanceByKey(processDefiKey);

		System.out.println("流程执行对象的id：" + pi.getId());// Execution 对象
		System.out.println("流程实例的id：" + pi.getProcessInstanceId());// ProcessInstance
		System.out.println("流程定义的id：" + pi.getProcessDefinitionId());// 默认执行的是最新版本的流程定义
	}
	
	@Test  // 查询正在运行任务
	public void queryTask() {
		TaskService taskService = processEngine.getTaskService();
		TaskQuery taskQuery = taskService.createTaskQuery();
		List<Task> list = taskQuery.list();
		if (list != null && list.size() > 0) {
			for (Task task : list) {
				System.out.println("任务的办理人：" + task.getAssignee());
				System.out.println("任务的id：" + task.getId());
				System.out.println("任务的名称：" + task.getName());
			}
		}
	}
	
	@Test   // 完成任务
	public void compileTask() {
		String taskId = "2404";
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("message", "不知道重不重要");
		
		processEngine.getTaskService().complete(taskId, params);
		System.out.println("当前任务执行完毕");
	}
	
	
}
