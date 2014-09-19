package org.primaldev.ppm.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

public abstract class ProcessUtil {
	
	
	public static TaskService getTaskService() {
		return ProcessEngines.getDefaultProcessEngine().getTaskService();
	}
	
	public static String getIdOfCurrentUser() {	
		String currentUser = null;
		try {
		    VaadinSession.getCurrent().getLockInstance().lock();
		    currentUser = (String) VaadinSession.getCurrent().getAttribute("myBite");
		} finally {
		    VaadinSession.getCurrent().getLockInstance().unlock();
		}
		
		return currentUser;
	}	
	

	public static String getFormKey(Task task) {
		return getFormService().getTaskFormData(task.getId()).getFormKey();
	}

	

	public static FormService getFormService() {
		return ProcessEngines.getDefaultProcessEngine().getFormService();
	}
	
	
	public static boolean taskHasForm(Task task) {
		return getFormService().getTaskFormData(task.getId()).getFormKey() != null;
	}
	

	public static RepositoryService getRepositoryService() {
		return ProcessEngines.getDefaultProcessEngine().getRepositoryService();
	}

	public static RuntimeService getRuntimeService() {
		return ProcessEngines.getDefaultProcessEngine().getRuntimeService();
	}
	
	
	//ProductName and ProductStatus must be hardcoded in the BPL
	
	public static String getProductName(Task task){
		return (String) ProcessUtil.getRuntimeService().getVariable(task.getExecutionId(), "productname");
	}
	
	public static String getProductStatus(Task task){
		return (String) ProcessUtil.getRuntimeService().getVariable(task.getExecutionId(), "productstatus");
	}
	
	public static HistoryService getHistoryService(){
		return ProcessEngines.getDefaultProcessEngine().getHistoryService();
	}

	
	public static IdentityService getIdentityService() {
		return ProcessEngines.getDefaultProcessEngine().getIdentityService();
	}
	
	
	
	public long getNumberOfTasksAssignedToCurrentUser() {
		String currentUser = ProcessUtil.getIdOfCurrentUser();
		TaskQuery query = ProcessUtil.getTaskService().createTaskQuery();
		return query.taskAssignee(currentUser).count();
	}
	
	public long getNumberOfUnassignedTasks() {
		String currentUser = ProcessUtil.getIdOfCurrentUser();
		TaskQuery query = ProcessUtil.getTaskService().createTaskQuery();
		return query.taskUnassigned().taskCandidateUser(currentUser).count();
	}
	
	public static Connection getCurrentDatabaseConnection() {
		return Context.getCommandContext().getDbSqlSession().getSqlSession().getConnection();
		}
		public static ResultSet executeSelectSqlQuery(String sql) throws Exception {
		Connection connection = getCurrentDatabaseConnection();
		Statement select = connection.createStatement();
		return select.executeQuery(sql);
		}

	public static List<ProcessDefinition> getAllProcessDefinitions() {
			ProcessDefinitionQuery query = getRepositoryService()
					.createProcessDefinitionQuery();
			return query.orderByProcessDefinitionName().asc().list();
		}
	
}
