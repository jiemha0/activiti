package com.jiem.activiti.leavebill;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

/**
 * Activiti 工作流框架
 * @author jiem 2017年10月21日
 * @version 1.0
 */
public class Leavebill {

	/**
	 * 通过代码形式创建流程引擎 2017年10月21日 下午5:48:50
	 */
	public ProcessEngine createProcessEngine() {
		// 取得ProcessEngineConfiguration对象,设置数据库连接属性
		ProcessEngineConfiguration engineConfiguration = ProcessEngineConfiguration
				.createStandaloneInMemProcessEngineConfiguration();
		engineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
		engineConfiguration
				.setJdbcUrl("jdbc:mysql:///activiti?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf8");
		engineConfiguration.setJdbcUsername("jiem");
		engineConfiguration.setJdbcPassword("jiem");
		engineConfiguration.setDatabaseSchemaUpdate("true");

		// 通过ProcessEngineConfiguration对象创建 ProcessEngine 对象
		ProcessEngine processEngine = engineConfiguration.buildProcessEngine();
		System.out.println("-----------------手动创建流程引擎创建成功!----------------");
		return processEngine;
	}

	/**
	 * 通过加载 activiti.cfg.xml 获取 流程引擎 和自动创建数据库及表 2017年10月21日 下午5:49:49
	 */
	public ProcessEngine createProcessEngineByXML() {
		ProcessEngineConfiguration engineConfiguration = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
		ProcessEngine processEngine = engineConfiguration.buildProcessEngine();
		System.out
				.println("-----------------读取配置文件创建流程引擎创建成功!----------------");
		return processEngine;
	}

	/**
	 * 通过ProcessEngines 来获取默认的流程引擎 2017年10月21日 下午5:50:10
	 */
	public ProcessEngine createProcessEngineByDefault() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		System.out.println("-----------------创建默认流程引擎创建成功!----------------");
		return processEngine;
	}

	/**
	 * 部署流程定义 2017年10月21日 下午5:51:00
	 */
	public Deployment deploy(ProcessEngine processEngine) {
		// 获取仓库服务 ：管理流程定义
		RepositoryService repositoryService = processEngine
				.getRepositoryService();
		Deployment deploy = repositoryService.createDeployment() // 创建一个部署的构建器
				.addClasspathResource("diagrams/LeaveBill.bpmn") // 从类路径中添加资源,一次只能添加一个资源
				.addClasspathResource("diagrams/LeaveBill.png") // 从类路径中添加资源,一次只能添加一个资源
				.name("请求单流程") // 设置部署的名称
				.category("办公类别") // 设置部署的类别
				.deploy();
		;

		System.out.println("部署的id: " + deploy.getId());
		System.out.println("部署的名称: " + deploy.getName());

		return deploy;
	}

	/**
	 * 执行流程 2017年10月21日 下午6:08:29
	 */
	public void startProcess(ProcessEngine processEngine, String processDefiKey) {

		// 取运行时服务
		RuntimeService runtimeService = processEngine.getRuntimeService();
		// 取得流程实例
		ProcessInstance pi = runtimeService
				.startProcessInstanceByKey(processDefiKey);// 通过流程定义的key 来执行流程

		System.out.println("流程实例id: " + pi.getId());// 流程实例id
		System.out.println("流程定义id: " + pi.getProcessDefinitionId());// 输出流程定义的id
	}

	/**
	 * 查询任务 2017年10月21日 下午6:12:15
	 */
	public List<Task> queryTask(ProcessEngine processEngine, String assignee) {

		// 取得任务服务
		TaskService taskService = processEngine.getTaskService();
		// 创建一个任务查询对象
		TaskQuery taskQuery = taskService.createTaskQuery();
		// 办理人的任务列表
		List<Task> list = taskQuery.taskAssignee(assignee)// 指定办理人
				.list();

		// 遍历任务列表
		if (list != null && list.size() > 0) {
			for (Task task : list) {
				System.out.println("任务的办理人：" + task.getAssignee());
				System.out.println("任务的id：" + task.getId());
				System.out.println("任务的名称：" + task.getName());

			}
		}
		return list;
	}

	// 执行任务
	public void compileTask(ProcessEngine processEngine, String taskId) {
		processEngine.getTaskService().complete(taskId);
		System.out.println("当前任务执行完毕");
	}

	@Test
	public void test() {
		Leavebill leavebill = new Leavebill();
		
		//创建流程引擎
		ProcessEngine processEngine = leavebill.createProcessEngineByXML();
		
		//部署流程
//		Deployment deploy = leavebill.deploy(processEngine);

		//执行流程
//		String processDefiKey = "leaveBill";
//		leavebill.startProcess(processEngine, processDefiKey);
		
        //查询任务
		String assignee = "张三";// 任务的办理人
		List<Task> queryTask = leavebill.queryTask(processEngine, assignee);
		for (Task task : queryTask) {
			leavebill.compileTask(processEngine, task.getId());//执行任务
		}
		
		String assignee2 = "李四";// 任务的办理人
		List<Task> queryTask2 = leavebill.queryTask(processEngine, assignee2);
		for (Task task : queryTask2) {
			leavebill.compileTask(processEngine, task.getId());//执行任务
		}
		
		String assignee3 = "王五";// 任务的办理人
		List<Task> queryTask3 = leavebill.queryTask(processEngine, assignee3);
		for (Task task : queryTask3) {
			leavebill.compileTask(processEngine, task.getId());//执行任务
		}
		
	}

}
