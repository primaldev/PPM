package org.primaldev.ppm.ui.task;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.task.Task;

//Convert List<Task> to List<TaskProd>
public class TaskProdList {

	List<Task> tasks;
	List<TaskProd> taskProd;
	
	public TaskProdList(List<Task> tasks) {
		this.tasks = tasks;
		convertList();	
	}
	
	private void convertList(){
		taskProd = new ArrayList<TaskProd>();
		
		if(tasks!=null && tasks.size()>0){
			for(Task task : tasks) {
				taskProd.add(new TaskProduct(task).getTaskProd());
			}
		}
		
	}
	
	
	public List<TaskProd> getTaskProd() {
		return taskProd;
	}
	
	public List<Task> getTasks() {
		return tasks;
	}
	
	public void setTaskProd(List<TaskProd> taskProd) {
		this.taskProd = taskProd;
	}
	
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	
}
