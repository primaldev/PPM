package org.primaldev.ppm.event;

import org.activiti.engine.repository.ProcessDefinition;

import com.github.wolfie.blackboard.Event;

public class SwitchView_Event implements Event{
	private String viewName = null;
	private ProcessDefinition processDefinition = null;
	
	public SwitchView_Event(String s) {
		viewName = s;
	}
	
	public SwitchView_Event(String s, ProcessDefinition p) {
		viewName = s;
		processDefinition=p;
	}

	public String getViewName() {
		return viewName;
	}
	
	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}
}
