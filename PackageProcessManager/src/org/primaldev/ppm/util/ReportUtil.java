package org.primaldev.ppm.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.workflow.simple.converter.WorkflowDefinitionConversion;
import org.activiti.workflow.simple.converter.WorkflowDefinitionConversionFactory;
import org.activiti.workflow.simple.definition.WorkflowDefinition;
import org.primaldev.ppm.ui.process.ProcessListUI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class ReportUtil {
	
	public static final String PROCESS_CATOGORY_NAME = "activiti-report";
	public static final String PROCESS_OVEVIEW_NAME = "_01 Process report";
	public static final String TAKS_DURATION_NAME = "_02 Task Duration";
	
	public static ProcessInstance generateTaskDurationReport(String processDefinitionId) {
		 
		 // Fetch process definition
		 ProcessDefinition processDefinition = ProcessUtil.getRepositoryService().createProcessDefinitionQuery()
		 .processDefinitionId(processDefinitionId).singleResult();
		 
		 if (processDefinition != null) { 
		 
		 // Report description
		 String reportDescription = "Average task duration report for process definition " + processDefinition.getName() + " ( version " + processDefinition.getVersion() + ")";
		 // Script (just plain String for the moment)
		 String script = "importPackage(java.sql);" +
		 "importPackage(java.lang);" +
		 "importPackage(org.primaldev.ppm.util);" +
		 "" +
		 "var processDefinitionId = \'" + processDefinitionId + "\';" +
		 "" +
		 "var result = ReportUtil.executeSelectSqlQuery(\"select NAME_, avg(DURATION_) from ACT_HI_TASKINST where PROC_DEF_ID_ = \'"
		 + processDefinitionId + "\' and END_TIME_ is not null group by NAME_\");" +
		 "" +
		 "var reportData = {};" +
		 "reportData.datasets = [];" +
		 "var dataset = {};" +
		  "dataset.type = \"pieChart\";" +
		 "dataset.description = '" + reportDescription + "';" +
		 "" +
		 "while (result.next()) { "+
		 " var name = result.getString(1);" +
		 " var val = result.getLong(2) / 1000;" +
		 "dataset.add(name, val);" +
		 "}" +
		 "" +
		 "execution.setVariable('reportData', reportData.toBytes());";	
		 
		 
		 return startReportProcess(TAKS_DURATION_NAME, reportDescription, script );
		 } 
		 
		 return null;
		    
	 }
	

public static ProcessInstance getProcessListDuration() {

	String reportDescription = "Process Overview";
	
	String script = "importPackage(java.sql);" +
	  "importPackage(java.lang);" +
	  "importPackage(org.primaldev.ppm.util);" + 	 
	  "var result = ReportUtil.executeSelectSqlQuery(\"SELECT PD.NAME_, PD.VERSION_ , count(*) FROM ACT_HI_PROCINST PI inner join ACT_RE_PROCDEF PD on PI.PROC_DEF_ID_ = PD.ID_ group by PROC_DEF_ID_\");" +
	 ""+
	  "var reportData = {};" +
	  "reportData.datasets = [];" +
	   ""+	  
	 "var dataset = {};" +
	  "dataset.type = \"pieChart\";" +
	  "dataset.description = \"Process instance overview (\" + new java.util.Date() + \")\";" + 
	  "dataset.data = {};"+
	 "" +
	  "while (result.next()) {" + // process results one row at a time
	    "var name = result.getString(1);" +
	    "var version = result.getLong(2);" +
	    "var count = result.getLong(3);" +
	    "dataset.data[name + \" (v\" + version + \")\"] = count;" +
	  "}" +
	  "reportData.datasets.push(dataset);" +
	  ""+
	  "execution.setVariable(\"reportData\", new java.lang.String(JSON.stringify(reportData)).getBytes(\"UTF-8\"));";
	
	return startReportProcess(PROCESS_OVEVIEW_NAME, reportDescription, script );
	
	  
}

public static ProcessInstance startReportProcess(String name,String reportDescription,String script ){
	
	 WorkflowDefinition workflowDefinition = new WorkflowDefinition()
     .name(name)
     .description(reportDescription)
     .addScriptStep(script);
	
	  
	 // Convert to BPMN 2.0 XML
	    WorkflowDefinitionConversionFactory convertFactory = new WorkflowDefinitionConversionFactory();
	    WorkflowDefinitionConversion conversion = convertFactory
	            .createWorkflowDefinitionConversion(workflowDefinition);
	    conversion.convert();
	    conversion.getBpmnModel().setTargetNamespace(PROCESS_CATOGORY_NAME);
	    
	    // Generate DI
	    BpmnAutoLayout bpmnAutoLayout = new BpmnAutoLayout(conversion.getBpmnModel());
	    bpmnAutoLayout.execute();
	    
	    String existingProcessId;
	   
	    
	  if (ProcessUtil.getRepositoryService().createProcessDefinitionQuery().processDefinitionName(name).singleResult() == null) {
	    
		  // Deploy
		  	Deployment result = ProcessUtil.getRepositoryService().createDeployment()
				  .name(name)
				  .addString(conversion.getProcess().getId() + ".bpmn20.xml", conversion.getBpmn20Xml())
				  .deploy();
		  	
		  	return ProcessUtil.getRuntimeService().startProcessInstanceById(
					   getProcessDefinition(result.getId()).getId());
	  }else{
		  existingProcessId = ProcessUtil.getRepositoryService().createProcessDefinitionQuery().processDefinitionName(name).singleResult().getId();
		  return ProcessUtil.getRuntimeService().startProcessInstanceById(existingProcessId);
	  
	  }
	
}


private static ProcessDefinition getProcessDefinition(String deploymentId) {
	ProcessDefinitionQuery query = ProcessUtil.getRepositoryService()
			.createProcessDefinitionQuery();
	return query.deploymentId(deploymentId).singleResult();
}
	  
	
  public static ResultSet executeSelectSqlQuery(String sql) throws Exception {
    
    Connection connection = getCurrentDatabaseConnection();
    Statement select = connection.createStatement();
    return select.executeQuery(sql);
  }
  

  public static Connection getCurrentDatabaseConnection() {
    return Context.getCommandContext().getDbSqlSession().getSqlSession().getConnection();
  }

}
