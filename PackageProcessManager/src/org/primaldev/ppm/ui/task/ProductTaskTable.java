package org.primaldev.ppm.ui.task;

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
		return new String[] {"product", "id", "name", "description", "priority",
				"dueDate", "createTime","status", "assignee" };
	}


	
	@SuppressWarnings("serial")
	private ColumnGenerator createProductNameColumnGenerator() {
		return new ColumnGenerator() {
			@Override
			public Component generateCell(Table source, Object itemId,
					Object columnId) {
				Task task = (Task) itemId;
				Label label = new Label(ProcessUtil.getProductName(task));				
				return label;
			}
			
		};
	}
	
	
	@SuppressWarnings("serial")
	private ColumnGenerator createProductStatusColumnGenerator() {
		return new ColumnGenerator() {
			@Override
			public Component generateCell(Table source, Object itemId,
					Object columnId) {
				Task task = (Task) itemId;
				Label label = new Label(ProcessUtil.getProductStatus(task));				
				return label;
			}
			
		};
	}	
	
	
	public List<TaskProd> getTasksToShow() {
		return tasksToShow;
	}
	public void setTasksToShow(List<TaskProd> tasksToShow) {
		this.tasksToShow = tasksToShow;	
		dataSource = new BeanItemContainer<TaskProd>(TaskProd.class);
		setContainerDataSource(dataSource);	
		//addGeneratedColumn("product", createProductNameColumnGenerator());	
		//addGeneratedColumn("status", createProductStatusColumnGenerator());	
		//setVisibleColumns(getAllVisibleColumns());
		setColumnHeader("name", "task name");		
		dataSource.removeAllItems();
		dataSource.addAll(this.tasksToShow);
	}


	
	
}
