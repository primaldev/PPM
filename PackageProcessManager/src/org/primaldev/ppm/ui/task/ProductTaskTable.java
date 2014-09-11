package org.primaldev.ppm.ui.task;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.activiti.engine.task.Task;
import org.primaldev.ppm.util.ProcessUtil;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Table;

public class ProductTaskTable extends Table {

	protected BeanItemContainer<TaskProd> dataSource;
	private List<TaskProd> tasksToShow;
	
	
	public ProductTaskTable() {
		
	}
	

	private String[] getAllVisibleColumns() {
		return new String[] {"id" ,"name", "productName", "description", "priority",
				"dueDate", "createTime","productStatus", "assignee" };
	}

	
	
	public List<TaskProd> getTasksToShow() {
		return tasksToShow;
	}
	
	public void setTasksToShow(List<TaskProd> tasksToShow) {
		this.tasksToShow = tasksToShow;	
		dataSource = new BeanItemContainer<TaskProd>(TaskProd.class);
		setContainerDataSource(dataSource);
		setVisibleColumns(getAllVisibleColumns());
		
		setColumnHeader("name", "task name");		
		dataSource.removeAllItems();
		dataSource.addAll(this.tasksToShow);
	}


	
	
}
