package org.primaldev.ppm.ui.task;



import java.util.Date;
import java.util.List;

import org.activiti.engine.task.Task;
import org.primaldev.ppm.util.ProcessUtil;

public class TaskProduct  implements TaskProd {

	String productName;
	String productStatus;
	Task task;
	
	public TaskProduct(Task task) {
		this.task=task;
		initValues();
	}
	
	private void initValues(){
		productName=ProcessUtil.getProductName(task);
		productStatus=ProcessUtil.getProductStatus(task);
	}
	
	
	/* (non-Javadoc)
	 * @see org.primaldev.ppm.ui.task.TaskProd#getProductName()
	 */
	
	public String getProductName() {
		return productName;
	}
	
	/* (non-Javadoc)
	 * @see org.primaldev.ppm.ui.task.TaskProd#getProductStatus()
	 */
	
	public String getProductStatus() {
		return productStatus;
	}
	
	/* (non-Javadoc)
	 * @see org.primaldev.ppm.ui.task.TaskProd#setProductName(java.lang.String)
	 */
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	/* (non-Javadoc)
	 * @see org.primaldev.ppm.ui.task.TaskProd#setProductStatus(java.lang.String)
	 */
	
	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	@Override
	public Task getTask() {
		// TODO Auto-generated method stub
		return task;
	}

	@Override
	public void setTask(Task task) {
		// TODO Auto-generated method stub
		this.task=task;
		initValues();
	}
	
	public TaskProd getTaskProd() {
		return this;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return task.getName();
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return task.getId();
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return task.getDescription();
	}

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return task.getPriority();
	}

	@Override
	public Date getDueDate() {
		// TODO Auto-generated method stub
		return task.getDueDate();
	}

	@Override
	public Date getCreateTime() {
		// TODO Auto-generated method stub
		return task.getCreateTime();
	}

	@Override
	public String getAssignee() {
		// TODO Auto-generated method stub
		return task.getAssignee();
	}

	@Override
	public boolean hasItemRegex(String searchString) {
		// TODO Auto-generated method stub
		
		if (getAssignee() !=null && getAssignee().matches(searchString)) return true;
		if (getDescription() !=null && getDescription().matches(searchString)) return true;
		if (getId() !=null && getId().matches(searchString)) return true;
		if (getName() !=null && getName().matches(searchString)) return true;
		if (getProductStatus() !=null && getProductStatus().matches(searchString)) return true;
		if (getProductName() !=null && getProductName().matches(searchString)) return true;
		
		return false;
	}

	

	
}
