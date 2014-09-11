package org.primaldev.ppm.ui.task;

import java.util.Date;

import org.activiti.engine.task.Task;



public interface TaskProd {

	
	
	public Task getTask();
	
	public void setTask(Task task);
	
	public String getProductName();

	public String getProductStatus();

	public void setProductName(String productName);

	public void setProductStatus(String productStatus);

	public String getName();

	public String getId();

	public String getDescription();

	public int getPriority();

	public Date getDueDate();

	public Date getCreateTime();

	public String getAssignee();
	
	public boolean hasItemRegex(String searchString);
	

}