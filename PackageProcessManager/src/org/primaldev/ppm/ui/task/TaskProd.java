package org.primaldev.ppm.ui.task;

import org.activiti.engine.task.Task;



public interface TaskProd extends Task{

	public String getProductName();

	public String getProductStatus();

	public void setProductName(String productName);

	public void setProductStatus(String productStatus);

}