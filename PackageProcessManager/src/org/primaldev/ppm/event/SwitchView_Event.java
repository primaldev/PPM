package org.primaldev.ppm.event;

import org.activiti.engine.repository.ProcessDefinition;

import com.github.wolfie.blackboard.Event;

public class SwitchView_Event implements Event{
	private String viewName = null;
	private ProcessDefinition processDefinition = null;
	private String closeView="";
	
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
	}

	public SwitchView_Event(String s, ProcessDefinition p, String closeView) {
		viewName = s;
		processDefinition=p;
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
	
	
	
}
