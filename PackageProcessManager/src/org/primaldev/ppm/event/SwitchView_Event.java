package org.primaldev.ppm.event;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;

import com.github.wolfie.blackboard.Event;

public class SwitchView_Event implements Event{
	private String viewName = null;
	private ProcessDefinition processDefinition = null;
	private String closeView="";
	private Task task = null;
	private boolean procType;
	
	public SwitchView_Event(String s) {
		viewName = s;
		
	}
	
	public SwitchView_Event(String s, String closeView) {
		viewName = s;
		this.closeView = closeView;
	}
	
	public SwitchView_Event(String s, ProcessDefinition p) {
		viewName = s;
		processDefinition=p;
		procType=true;
	}

	public SwitchView_Event(String s, ProcessDefinition p, String closeView) {
		viewName = s;
		processDefinition=p;
		procType=true;
		this.closeView = closeView;
	}
	
	public SwitchView_Event(String s, Task t) {
		viewName = s;
		task=t;	
		procType=false;
	}
	
	public SwitchView_Event(String s, Task t, String closeView) {
		viewName = s;
		task=t;
		procType=false;
		this.closeView = closeView;
	}
	

	public String getViewName() {
		return viewName;
	}
	
	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}	
	
	public String getCloseView() {
		return closeView;
	}

	public boolean isCloseView(){
		if (closeView.isEmpty()){
			return false;
		}else{
			return true;
		}
	}

	public Task getTask() {
		return task;
	}

	public boolean isProcType() {
		return procType;
	}

	public void setProcType(boolean procType) {
		this.procType = procType;
	}


	
	
	
}
