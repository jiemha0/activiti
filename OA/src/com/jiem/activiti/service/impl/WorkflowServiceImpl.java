package com.jiem.activiti.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;

import com.jiem.activiti.dao.ILeaveBillDao;
import com.jiem.activiti.domain.LeaveBill;
import com.jiem.activiti.service.IWorkflowService;
import com.jiem.activiti.util.SessionContext;
import com.jiem.activiti.web.WorkflowBean;


public class WorkflowServiceImpl implements IWorkflowService {
	/**�������Dao*/
	private ILeaveBillDao leaveBillDao;
	
	private RepositoryService repositoryService;
	
	private RuntimeService runtimeService;
	
	private TaskService taskService;
	
	private FormService formService;
	
	private HistoryService historyService;

	public void setLeaveBillDao(ILeaveBillDao leaveBillDao) {
		this.leaveBillDao = leaveBillDao;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}
	
	public void setFormService(FormService formService) {
		this.formService = formService;
	}
	
	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	/**�������̶���*/
	@Override
	public void saveNewDeploye(File file, String filename) {
		try {
			//2����File���͵��ļ�ת����ZipInputStream��
			ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
			repositoryService.createDeployment()//�����������
							.name(filename)//��Ӳ�������
							.addZipInputStream(zipInputStream)//
							.deploy();//��ɲ���
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**��ѯ���������Ϣ����Ӧ��act_re_deployment��*/
	@Override
	public List<Deployment> findDeploymentList() {
		List<Deployment> list = repositoryService.createDeploymentQuery()//������������ѯ
							.orderByDeploymenTime().asc()//
							.list();
		return list;
	}
	
	/**��ѯ���̶������Ϣ����Ӧ��act_re_procdef��*/
	@Override
	public List<ProcessDefinition> findProcessDefinitionList() {
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()//�������̶����ѯ
							.orderByProcessDefinitionVersion().asc()//
							.list();
		return list;
	}
	
	/**ʹ�ò������ID����ԴͼƬ���ƣ���ȡͼƬ��������*/
	@Override
	public InputStream findImageInputStream(String deploymentId,
			String imageName) {
		return repositoryService.getResourceAsStream(deploymentId, imageName);
	}
	
	/**ʹ�ò������ID��ɾ�����̶���*/
	@Override
	public void deleteProcessDefinitionByDeploymentId(String deploymentId) {
		repositoryService.deleteDeployment(deploymentId, true);
	}
	
	/**�������״̬����������ʵ����������������ʵ������ҵ��*/
	@Override
	public void saveStartProcess(WorkflowBean workflowBean) {
		//1����ȡ��ٵ�ID��ʹ����ٵ�ID����ѯ��ٵ��Ķ���LeaveBill
		Long id = workflowBean.getId();
		LeaveBill leaveBill = leaveBillDao.getLeaveBillById(id);
		//2��������ٵ������״̬��0���1����ʼ¼��-->����У�
		leaveBill.setState(1);
		//3��ʹ�õ�ǰ�����ȡ�����̶����key����������ƾ������̶����key��
		String key = leaveBill.getClass().getSimpleName();
		/**
		 * 4����Session�л�ȡ��ǰ����İ����ˣ�ʹ�����̱���������һ������İ�����
			    * inputUser�����̱��������ƣ�
			    * ��ȡ�İ����������̱�����ֵ
		 */
		Map<String, Object> variables = new HashMap<String,Object>();
		variables.put("inputUser", SessionContext.getUser().getName());//��ʾΩһ�û�
		/**
		 * 5��	(1)ʹ�����̱��������ַ�������ʽ��LeaveBill.id����ʽ����ͨ�����ã������������̣�����ʵ��������ҵ��
   				(2)ʹ������ִ�ж�����е�һ���ֶ�BUSINESS_KEY��Activiti�ṩ��һ���ֶΣ��������������̣�����ʵ��������ҵ��
		 */
		//��ʽ��LeaveBill.id����ʽ��ʹ�����̱�����
		String objId = key+"."+id;
		variables.put("objId", objId);
		//6��ʹ�����̶����key����������ʵ����ͬʱ�������̱�����ͬʱ������ִ�е�ִ�ж�����е��ֶ�BUSINESS_KEY���ҵ�����ݣ�ͬʱ�����̹���ҵ��
		runtimeService.startProcessInstanceByKey(key,objId,variables);
		
	}
	
	/**2��ʹ�õ�ǰ�û�����ѯ����ִ�е��������ȡ��ǰ����ļ���List<Task>*/
	@Override
	public List<Task> findTaskListByName(String name) {
		List<Task> list = taskService.createTaskQuery()//
					.taskAssignee(name)//ָ�����������ѯ
					.orderByTaskCreateTime().asc()//
					.list();
		return list;
	}
	
	/**ʹ������ID����ȡ��ǰ����ڵ��ж�Ӧ��Form key�е����ӵ�ֵ*/
	@Override
	public String findTaskFormKeyByTaskId(String taskId) {
		TaskFormData formData = formService.getTaskFormData(taskId);
		//��ȡForm key��ֵ
		String url = formData.getFormKey();
		return url;
	}
	
	/**һ��ʹ������ID��������ٵ�ID���Ӷ���ȡ��ٵ���Ϣ*/
	@Override
	public LeaveBill findLeaveBillByTaskId(String taskId) {
		//1��ʹ������ID����ѯ�������Task
		Task task = taskService.createTaskQuery()//
						.taskId(taskId)//ʹ������ID��ѯ
						.singleResult();
		//2��ʹ���������Task��ȡ����ʵ��ID
		String processInstanceId = task.getProcessInstanceId();
		//3��ʹ������ʵ��ID����ѯ����ִ�е�ִ�ж������������ʵ������
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
						.processInstanceId(processInstanceId)//ʹ������ʵ��ID��ѯ
						.singleResult();
		//4��ʹ������ʵ�������ȡBUSINESS_KEY
		String buniness_key = pi.getBusinessKey();
		//5����ȡBUSINESS_KEY��Ӧ������ID��ʹ������ID����ѯ��ٵ�����LeaveBill.1��
		String id = "";
		if(StringUtils.isNotBlank(buniness_key)){
			//��ȡ�ַ�����ȡbuniness_keyС����ĵ�2��ֵ
			id = buniness_key.split("\\.")[1];
		}
		//��ѯ��ٵ�����
		//ʹ��hql��䣺from LeaveBill o where o.id=1
		LeaveBill leaveBill = leaveBillDao.getLeaveBillById(Long.parseLong(id));
		return leaveBill;
	}
	
	/**������֪����ID����ѯProcessDefinitionEntiy���󣬴Ӷ���ȡ��ǰ�������֮����������ƣ������õ�List<String>������*/
	@Override
	public List<String> findOutComeListByTaskId(String taskId) {
		//���ش�����ߵ����Ƽ���
		List<String> list = new ArrayList<String>();
		//1:ʹ������ID����ѯ�������
		Task task = taskService.createTaskQuery()//
					.taskId(taskId)//ʹ������ID��ѯ
					.singleResult();
		//2����ȡ���̶���ID
		String processDefinitionId = task.getProcessDefinitionId();
		//3����ѯProcessDefinitionEntiy����
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
		//ʹ���������Task��ȡ����ʵ��ID
		String processInstanceId = task.getProcessInstanceId();
		//ʹ������ʵ��ID����ѯ����ִ�е�ִ�ж������������ʵ������
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
					.processInstanceId(processInstanceId)//ʹ������ʵ��ID��ѯ
					.singleResult();
		//��ȡ��ǰ���id
		String activityId = pi.getActivityId();
		//4����ȡ��ǰ�Ļ
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
		//5����ȡ��ǰ����֮�����ߵ�����
		List<PvmTransition> pvmList = activityImpl.getOutgoingTransitions();
		if(pvmList!=null && pvmList.size()>0){
			for(PvmTransition pvm:pvmList){
				String name = (String) pvm.getProperty("name");
				if(StringUtils.isNotBlank(name)){
					list.add(name);
				}
				else{
					list.add("Ĭ���ύ");
				}
			}
		}
		return list;
	}
	
	/**ָ�����ߵ������������*/
	@Override
	public void saveSubmitTask(WorkflowBean workflowBean) {
		//��ȡ����ID
		String taskId = workflowBean.getTaskId();
		//��ȡ���ߵ�����
		String outcome = workflowBean.getOutcome();
		//��ע��Ϣ
		String message = workflowBean.getComment();
		//��ȡ��ٵ�ID
		Long id = workflowBean.getId();
		
		/**
		 * 1�������֮ǰ�����һ����ע��Ϣ����act_hi_comment����������ݣ����ڼ�¼�Ե�ǰ�����˵�һЩ�����Ϣ
		 */
		//ʹ������ID����ѯ������󣬻�ȡ��������ʵ��ID
		Task task = taskService.createTaskQuery()//
						.taskId(taskId)//ʹ������ID��ѯ
						.singleResult();
		//��ȡ����ʵ��ID
		String processInstanceId = task.getProcessInstanceId();
		/**
		 * ע�⣺�����ע��ʱ������Activiti�ײ������ʹ�ã�
		 * 		String userId = Authentication.getAuthenticatedUserId();
			    CommentEntity comment = new CommentEntity();
			    comment.setUserId(userId);
			  ������Ҫ��Session�л�ȡ��ǰ��¼�ˣ���Ϊ������İ����ˣ�����ˣ�����Ӧact_hi_comment���е�User_ID���ֶΣ��������������ˣ����ֶ�Ϊnull
			 ����Ҫ���������ִ��ʹ��Authentication.setAuthenticatedUserId();��ӵ�ǰ����������
		 * */
		Authentication.setAuthenticatedUserId(SessionContext.getUser().getName());
		taskService.addComment(taskId, processInstanceId, message);
		/**
		 * 2��������ߵ������ǡ�Ĭ���ύ������ô�Ͳ���Ҫ���ã�������ǣ�����Ҫ�������̱���
		 * ���������֮ǰ���������̱������������ߵ����ƣ�ȥ�������
				 ���̱��������ƣ�outcome
				 ���̱�����ֵ�����ߵ�����
		 */
		Map<String, Object> variables = new HashMap<String,Object>();
		if(outcome!=null && !outcome.equals("Ĭ���ύ")){
			variables.put("outcome", outcome);
		}

		//3��ʹ������ID����ɵ�ǰ�˵ĸ�������ͬʱ���̱���
		taskService.complete(taskId, variables);
		//4�����������֮����Ҫָ����һ������İ����ˣ�ʹ���ࣩ-----�Ѿ��������
		
		/**
		 * 5�����������֮���ж������Ƿ����
   			������̽����ˣ�������ٵ����״̬��1���2�������-->�����ɣ�
		 */
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
						.processInstanceId(processInstanceId)//ʹ������ʵ��ID��ѯ
						.singleResult();
		//���̽�����
		if(pi==null){
			//������ٵ����״̬��1���2�������-->�����ɣ�
			LeaveBill bill = leaveBillDao.getLeaveBillById(id);
			bill.setState(2);
		}
	}
	
	/**��ȡ��ע��Ϣ�����ݵ��ǵ�ǰ����ID����ȡ��ʷ����ID��Ӧ����ע*/
	@Override
	public List<Comment> findCommentByTaskId(String taskId) {
		List<Comment> list = new ArrayList<Comment>();
		//ʹ�õ�ǰ������ID����ѯ��ǰ���̶�Ӧ����ʷ����ID
		//ʹ�õ�ǰ����ID����ȡ��ǰ�������
		Task task = taskService.createTaskQuery()//
				.taskId(taskId)//ʹ������ID��ѯ
				.singleResult();
		//��ȡ����ʵ��ID
		String processInstanceId = task.getProcessInstanceId();
//		//ʹ������ʵ��ID����ѯ��ʷ���񣬻�ȡ��ʷ�����Ӧ��ÿ������ID
//		List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()//��ʷ������ѯ
//						.processInstanceId(processInstanceId)//ʹ������ʵ��ID��ѯ
//						.list();
//		//�������ϣ���ȡÿ������ID
//		if(htiList!=null && htiList.size()>0){
//			for(HistoricTaskInstance hti:htiList){
//				//����ID
//				String htaskId = hti.getId();
//				//��ȡ��ע��Ϣ
//				List<Comment> taskList = taskService.getTaskComments(htaskId);//������ʷ��ɺ������ID
//				list.addAll(taskList);
//			}
//		}
		list = taskService.getProcessInstanceComments(processInstanceId);
		return list;
	}
	
	/**ʹ����ٵ�ID����ѯ��ʷ��ע��Ϣ*/
	@Override
	public List<Comment> findCommentByLeaveBillId(Long id) {
		//ʹ����ٵ�ID����ѯ��ٵ�����
		LeaveBill leaveBill = leaveBillDao.getLeaveBillById(id);
		//��ȡ���������
		String objectName = leaveBill.getClass().getSimpleName();
		//��֯���̱��е��ֶ��е�ֵ
		String objId = objectName+"."+id;
		
		/**1:ʹ����ʷ������ʵ����ѯ��������ʷ������ʵ�����󣬻�ȡ����ʵ��ID*/
//		HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()//��Ӧ��ʷ������ʵ����
//						.processInstanceBusinessKey(objId)//ʹ��BusinessKey�ֶβ�ѯ
//						.singleResult();
//		//����ʵ��ID
//		String processInstanceId = hpi.getId();
		/**2:ʹ����ʷ�����̱�����ѯ��������ʷ�����̱����Ķ��󣬻�ȡ����ʵ��ID*/
		HistoricVariableInstance hvi = historyService.createHistoricVariableInstanceQuery()//��Ӧ��ʷ�����̱�����
						.variableValueEquals("objId", objId)//ʹ�����̱��������ƺ����̱�����ֵ��ѯ
						.singleResult();
		//����ʵ��ID
		String processInstanceId = hvi.getProcessInstanceId();
		List<Comment> list = taskService.getProcessInstanceComments(processInstanceId);
		return list;
	}
	
	/**1����ȡ����ID����ȡ�������ʹ����������ȡ���̶���ID����ѯ���̶������*/
	@Override
	public ProcessDefinition findProcessDefinitionByTaskId(String taskId) {
		//ʹ������ID����ѯ�������
		Task task = taskService.createTaskQuery()//
					.taskId(taskId)//ʹ������ID��ѯ
					.singleResult();
		//��ȡ���̶���ID
		String processDefinitionId = task.getProcessDefinitionId();
		//��ѯ���̶���Ķ���
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()//�������̶����ѯ���󣬶�Ӧ��act_re_procdef 
					.processDefinitionId(processDefinitionId)//ʹ�����̶���ID��ѯ
					.singleResult();
		return pd;
	}
	
	/**
	 * �����鿴��ǰ�����ȡ���ڻ��Ӧ������x,y,width,height����4��ֵ��ŵ�Map<String,Object>��
		 map���ϵ�key����ʾ����x,y,width,height
		 map���ϵ�value����ʾ�����Ӧ��ֵ
	 */
	@Override
	public Map<String, Object> findCoordingByTask(String taskId) {
		//�������
		Map<String, Object> map = new HashMap<String,Object>();
		//ʹ������ID����ѯ�������
		Task task = taskService.createTaskQuery()//
					.taskId(taskId)//ʹ������ID��ѯ
					.singleResult();
		//��ȡ���̶����ID
		String processDefinitionId = task.getProcessDefinitionId();
		//��ȡ���̶����ʵ����󣨶�Ӧ.bpmn�ļ��е����ݣ�
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(processDefinitionId);
		//����ʵ��ID
		String processInstanceId = task.getProcessInstanceId();
		//ʹ������ʵ��ID����ѯ����ִ�е�ִ�ж������ȡ��ǰ���Ӧ������ʵ������
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//��������ʵ����ѯ
					.processInstanceId(processInstanceId)//ʹ������ʵ��ID��ѯ
					.singleResult();
		//��ȡ��ǰ���ID
		String activityId = pi.getActivityId();
		//��ȡ��ǰ�����
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);//�ID
		//��ȡ����
		map.put("x", activityImpl.getX());
		map.put("y", activityImpl.getY());
		map.put("width", activityImpl.getWidth());
		map.put("height", activityImpl.getHeight());
		return map;
	}
}
