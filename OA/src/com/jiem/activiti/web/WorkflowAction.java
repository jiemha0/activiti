package com.jiem.activiti.web;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.struts2.ServletActionContext;

import com.jiem.activiti.domain.LeaveBill;
import com.jiem.activiti.service.ILeaveBillService;
import com.jiem.activiti.service.IWorkflowService;
import com.jiem.activiti.util.SessionContext;
import com.jiem.activiti.util.ValueContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
public class WorkflowAction extends ActionSupport implements ModelDriven<WorkflowBean> {

	private WorkflowBean workflowBean = new WorkflowBean();
	
	@Override
	public WorkflowBean getModel() {
		return workflowBean;
	}
	
	private IWorkflowService workflowService;
	
	private ILeaveBillService leaveBillService;

	public void setLeaveBillService(ILeaveBillService leaveBillService) {
		this.leaveBillService = leaveBillService;
	}

	public void setWorkflowService(IWorkflowService workflowService) {
		this.workflowService = workflowService;
	}

	/**
	 * ���������ҳ��ʾ
	 * @return
	 */
	public String deployHome(){
		//1:��ѯ���������Ϣ����Ӧ��act_re_deployment��
		List<Deployment> depList = workflowService.findDeploymentList();
		//2:��ѯ���̶������Ϣ����Ӧ��act_re_procdef��
		List<ProcessDefinition> pdList = workflowService.findProcessDefinitionList();
		//���õ������Ķ�����
		ValueContext.putValueContext("depList", depList);
		ValueContext.putValueContext("pdList", pdList);
		return "deployHome";
	}
	
	/**
	 * ��������
	 * @return
	 */
	public String newdeploy(){
		//��ȡҳ�洫�ݵ�ֵ
		//1����ȡҳ���ϴ��ݵ�zip��ʽ���ļ�����ʽ��File����
		File file = workflowBean.getFile();
		//�ļ�����
		String filename = workflowBean.getFilename();
		//��ɲ���
		workflowService.saveNewDeploye(file,filename);
		return "list";
	}
	
	/**
	 * ɾ��������Ϣ
	 */
	public String delDeployment(){
		//1����ȡ�������ID
		String deploymentId = workflowBean.getDeploymentId();
		//2��ʹ�ò������ID��ɾ�����̶���
		workflowService.deleteProcessDefinitionByDeploymentId(deploymentId);
		return "list";
	}
	
	/**
	 * �鿴����ͼ
	 * @throws Exception 
	 */
	public String viewImage() throws Exception{
		//1����ȡҳ�洫�ݵĲ������ID����ԴͼƬ����
		//�������ID
		String deploymentId = workflowBean.getDeploymentId();
		//��ԴͼƬ����
		String imageName = workflowBean.getImageName();
		//2����ȡ��Դ�ļ���act_ge_bytearray������ԴͼƬ������InputStream
		InputStream in = workflowService.findImageInputStream(deploymentId,imageName);
		//3����response�����ȡ�����
		OutputStream out = ServletActionContext.getResponse().getOutputStream();
		//4�����������е����ݶ�ȡ������д���������
		for(int b=-1;(b=in.read())!=-1;){
			out.write(b);
		}
		out.close();
		in.close();
		//��ͼд��ҳ���ϣ��������д
		return null;
	}
	
	// ��������
	public String startProcess(){
		//�������״̬����������ʵ����������������ʵ������ҵ��
		workflowService.saveStartProcess(workflowBean);
		return "listTask";
	}
	
	
	
	/**
	 * ���������ҳ��ʾ
	 * @return
	 */
	public String listTask(){
		//1����Session�л�ȡ��ǰ�û���
		String name = SessionContext.getUser().getName();
		//2��ʹ�õ�ǰ�û�����ѯ����ִ�е��������ȡ��ǰ����ļ���List<Task>
		List<Task> list = workflowService.findTaskListByName(name); 
		ValueContext.putValueContext("list", list);
		return "task";
	}
	
	/**
	 * �������
	 */
	public String viewTaskForm(){
		//����ID
		String taskId = workflowBean.getTaskId();
		//��ȡ�����������ڵ��url����
		String url = workflowService.findTaskFormKeyByTaskId(taskId);
		url += "?taskId="+taskId;
		ValueContext.putValueContext("url", url);
		return "viewTaskForm";
	}
	
	// ׼��������
	public String audit(){
		//��ȡ����ID
		String taskId = workflowBean.getTaskId();
		/**һ��ʹ������ID��������ٵ�ID���Ӷ���ȡ��ٵ���Ϣ*/
		LeaveBill leaveBill = workflowService.findLeaveBillByTaskId(taskId);
		ValueContext.putValueStack(leaveBill);
		/**������֪����ID����ѯProcessDefinitionEntiy���󣬴Ӷ���ȡ��ǰ�������֮����������ƣ������õ�List<String>������*/
		List<String> outcomeList = workflowService.findOutComeListByTaskId(taskId);
		ValueContext.putValueContext("outcomeList", outcomeList);
		/**������ѯ������ʷ����˵������Ϣ��������ǰ�������ˣ�����List<Comment>*/
		List<Comment> commentList = workflowService.findCommentByTaskId(taskId);
		ValueContext.putValueContext("commentList", commentList);
		return "taskForm";
	}
	
	/**
	 * �ύ����
	 */
	public String submitTask(){
		workflowService.saveSubmitTask(workflowBean);
		return "listTask";
	}
	
	/**
	 * �鿴��ǰ����ͼ���鿴��ǰ��ڵ㣬��ʹ�ú�ɫ�Ŀ��ע��
	 */
	public String viewCurrentImage(){
		//����ID
		String taskId = workflowBean.getTaskId();
		/**һ���鿴����ͼ*/
		//1����ȡ����ID����ȡ�������ʹ����������ȡ���̶���ID����ѯ���̶������
		ProcessDefinition pd = workflowService.findProcessDefinitionByTaskId(taskId);
		//workflowAction_viewImage?deploymentId=<s:property value='#deploymentId'/>&imageName=<s:property value='#imageName'/>
		ValueContext.putValueContext("deploymentId", pd.getDeploymentId());
		ValueContext.putValueContext("imageName", pd.getDiagramResourceName());
		/**�����鿴��ǰ�����ȡ���ڻ��Ӧ������x,y,width,height����4��ֵ��ŵ�Map<String,Object>��*/
		Map<String, Object> map = workflowService.findCoordingByTask(taskId);
		ValueContext.putValueContext("acs", map);
		return "image";
	}
	
	// �鿴��ʷ����ע��Ϣ
	public String viewHisComment(){
		//��ȡ�嵥ID
		Long id = workflowBean.getId();
		//1��ʹ����ٵ�ID����ѯ��ٵ����󣬽�������õ�ջ����֧�ֱ�����
		LeaveBill leaveBill = leaveBillService.getLeaveBillById(id);
		ValueContext.putValueStack(leaveBill);
		//2��ʹ����ٵ�ID����ѯ��ʷ����ע��Ϣ
		List<Comment> commentList = workflowService.findCommentByLeaveBillId(id);
		ValueContext.putValueContext("commentList", commentList);
		return "viewHisComment";
	}
}
