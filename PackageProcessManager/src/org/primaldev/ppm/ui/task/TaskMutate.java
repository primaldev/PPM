package org.primaldev.ppm.ui.task;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.task.Task;

public class TaskMutate {

	public TaskMutate() {
		// TODO Auto-generated constructor stub
	}
	
	
	public List<TaskProd> getTaskProd(List<Task> tasks) {
		
		List<TaskProd> taskProd = new ArrayList<TaskProd>();
		
		for (Task task : tasks) {
			
			taskProd.add((TaskProd) task);
		}
		
		
		return taskProd;
		
	}
	
	static TaskProd mutate(Task task){
		return   (TaskProd) task;
		
	}

} 
