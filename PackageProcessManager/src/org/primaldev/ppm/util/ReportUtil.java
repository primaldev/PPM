package org.primaldev.ppm.util;

import java.util.List;

import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.workflow.simple.converter.WorkflowDefinitionConversion;
import org.activiti.workflow.simple.converter.WorkflowDefinitionConversionFactory;
import org.activiti.workflow.simple.definition.WorkflowDefinition;
import org.primaldev.ppm.ui.process.ProcessListUI;

public class ReportUtil {
	
	public static void generateTaskDurationReport(String processDefinitionId) {
		 
		 // Fetch process definition
		 ProcessDefinition processDefinition = ProcessUtil.getRepositoryService().createProcessDefinitionQuery()
		 .processDefinitionId(processDefinitionId).singleResult();
		 // Report descriptin
		 String reportDescription = "Average task duration report for process definition " + processDefinition.getName() + " ( version " + processDefinition.getVersion() + ")";
		 // Script (just plain String for the moment)
		 String script = "importPackage(java.sql);" +
		 "importPackage(java.lang);" +
		 "importPackage(org.activiti.explorer.reporting);" +
		 "" +
		 "var processDefinitionId = '" + processDefinitionId + "';" +
		 "" +
		 "var result = ReportingUtil.executeSelectSqlQuery(\"select NAME_, avg(DURATION_) from ACT_HI_TASKINST where PROC_DEF_ID_ = '"
		 + processDefinitionId + "' and END_TIME_ is not null group by NAME_\");" +
		 "" +
		 "var reportData = new ReportData();" +
		 "var dataset = reportData.newDataset();" +
		 "dataset.type = 'pieChart';" +
		 "dataset.description = '" + reportDescription + "';" +
		 "" +
		 "while (result.next()) { "+
		 " var name = result.getString(1);" +
		 " var val = result.getLong(2) / 1000;" +
		 " dataset.add(name, val);" +
		 "}" +
		 "" +
		 "execution.setVariable('reportData', reportData.toBytes());";	
		 
		    // Generate bpmn model
		    WorkflowDefinition workflowDefinition = new WorkflowDefinition()
		      .name(processDefinition.getName() + " task duration report")
		      .description(reportDescription)
		      .addScriptStep(script);
		    
		    // Convert to BPMN 2.0 XML
		    WorkflowDefinitionConversionFactory convertFactory = new WorkflowDefinitionConversionFactory();
		    WorkflowDefinitionConversion conversion = convertFactory
		            .createWorkflowDefinitionConversion(workflowDefinition);
		    conversion.convert();
		    conversion.getBpmnModel().setTargetNamespace("activiti-report");
		    
		    // Generate DI
		    BpmnAutoLayout bpmnAutoLayout = new BpmnAutoLayout(conversion.getBpmnModel());
		    bpmnAutoLayout.execute();
		    
		    // Deploy
		    ProcessUtil.getRepositoryService().createDeployment()
		      .name(processDefinition.getName() + " - task duration report")
		      .addString(conversion.getProcess().getId() + ".bpmn20.xml", conversion.getBpmn20Xml())
		      .deploy();
		    
		    //Start (No form) //code not good, starting wrong process.
		  //  ProcessUtil.getRuntimeService().startProcessInstanceById(
			//		processDefinition.getId());
		    
	 }
	

public static ProcessInstance getProcessListDuration() {

	 String reportDescription = "Process Overview";
	
	String script = "importPackage(java.sql);" +
	  "importPackage(java.lang);" +
	  "importPackage(org.activiti.explorer.reporting);" + 	 
	  "var result = ReportingUtil.executeSelectSqlQuery(\"SELECT PD.NAME_, PD.VERSION_ , count(*) FROM ACT_HI_PROCINST PI" + 
	       "inner join ACT_RE_PROCDEF PD on PI.PROC_DEF_ID_ = PD.ID_ group by PROC_DEF_ID_\");" +
	 ""+
	  "var reportData = {};" +
	  "reportData.datasets = [];" +
	   ""+	  
	 "var dataset = reportData.newDataset();" +
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
	
	
	 WorkflowDefinition workflowDefinition = new WorkflowDefinition()
     .name("Process report")
     .description(reportDescription)
     .addScriptStep(script);
	 
	 // Convert to BPMN 2.0 XML
	    WorkflowDefinitionConversionFactory convertFactory = new WorkflowDefinitionConversionFactory();
	    WorkflowDefinitionConversion conversion = convertFactory
	            .createWorkflowDefinitionConversion(workflowDefinition);
	    conversion.convert();
	    conversion.getBpmnModel().setTargetNamespace("activiti-report");
	    
	    // Generate DI
	    BpmnAutoLayout bpmnAutoLayout = new BpmnAutoLayout(conversion.getBpmnModel());
	    bpmnAutoLayout.execute();
	    
	    // Deploy
	   Deployment result = ProcessUtil.getRepositoryService().createDeployment()
	      .name("Process duration report")
	      .addString(conversion.getProcess().getId() + ".bpmn20.xml", conversion.getBpmn20Xml())
	      .deploy();
	    
	   
	   
	    //Start (No form) 
	   return ProcessUtil.getRuntimeService().startProcessInstanceById(
			   getProcessDefinition(result.getId()).getId());
	  
	 
	  
}
	  
private static ProcessDefinition getProcessDefinition(String deploymentId) {
	ProcessDefinitionQuery query = ProcessUtil.getRepositoryService()
			.createProcessDefinitionQuery();
	return query.orderByProcessDefinitionName().deploymentId(deploymentId).singleResult();
}
	  
	

}
