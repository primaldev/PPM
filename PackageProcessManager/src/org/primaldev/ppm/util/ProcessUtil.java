package org.primaldev.ppm.util;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

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

	
}
