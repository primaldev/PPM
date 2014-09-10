package org.primaldev.ppm.ui.task;



import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.Task;

public abstract class TaskProduct extends TaskEntity implements TaskProd {

	String productName;
	String productStatus;
	
	public TaskProduct() {
		// TODO Auto-generated constructor stub
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
	

}
