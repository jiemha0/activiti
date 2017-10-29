package com.jiem.activiti.deploy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class DeploymentTest {

	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	//@Test //部署流程定义，资源来在bpmn格式
	public void  deployProcessDeploy() {
		
		Deployment deploy = processEngine.getRepositoryService()
		.createDeployment()
		.name("采购流程")
		.category("采购类别") // 设置部署的类别
		.addClasspathResource("diagrams/BuyBill.bpmn")
		.addClasspathResource("diagrams/BuyBill.png")
		.deploy();
		
		System.out.println("部署名称:"+deploy.getName());
		System.out.println("部署id:"+deploy.getId());
	}
	
	//@Test //部署流程定义，资源来在bpmn格式
	public void deployProcessDeployByZip() {
		
		InputStream resourceAsStream = getClass()
				.getClassLoader()
				.getResourceAsStream("diagrams/BuyBill.zip");
		
		Deployment deploy = processEngine.getRepositoryService()
				.createDeployment()
				.name("采购流程")
				.addZipInputStream(new ZipInputStream(resourceAsStream))
				.deploy();
		
		System.out.println("部署名称:"+deploy.getName());
		System.out.println("部署id:"+deploy.getId());
	}
	
	
	@Test  //查看流程定义
	public void queryProcessDefination(){
		String processDefinationKey = "buyBill";
		List<ProcessDefinition> list = processEngine.getRepositoryService()
				.createProcessDefinitionQuery()
				.processDefinitionKey(processDefinationKey)
				.latestVersion()
				.orderByProcessDefinitionVersion().desc()
				.list();
		
		for (ProcessDefinition temp : list) {
			System.out.print("流程定义的id: "+temp.getId());
			System.out.print(" 流程定义的key: "+temp.getKey());
			System.out.print(" 流程定义的版本: "+temp.getVersion());
			System.out.print(" 流程定义部署的id: "+temp.getDeploymentId());
			System.out.println(" 流程定义的名称: "+temp.getName());
		}
	}
	
	
	@Test //查看bpmn 资源图片
	public void viewImage() throws IOException{
		String deploymentId = "705";
		String imageName = null ;
		
		List<String> deploymentResourceNames = processEngine.getRepositoryService()
					.getDeploymentResourceNames(deploymentId);
		
		if( deploymentResourceNames != null && deploymentResourceNames.size()> 0 ){
			for (String temp : deploymentResourceNames) {
				if(temp.indexOf(".png")>0){
					imageName=temp;
				}
			}
		}
		
		InputStream resourceAsStream = processEngine.getRepositoryService()
					.getResourceAsStream(deploymentId, imageName);
		
		FileUtils.copyInputStreamToFile(resourceAsStream, new File("D:/1temp/"+imageName));
		
		System.out.println("图片已保存到 ： D:/1temp/"+imageName);
	}
	
	
	
		@Test //删除流程定义
		public void deleteProcessDefi(){
			String deploymentId="501"; //通过部署id来删除流程定义
			
			processEngine.getRepositoryService().deleteDeployment(deploymentId);
			System.out.println("删除流程定义的id为 "+deploymentId+"成功");
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
