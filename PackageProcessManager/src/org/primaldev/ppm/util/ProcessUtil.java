package org.primaldev.ppm.util;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

import com.vaadin.server.VaadinSession;

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
	

	
	
}
