package org.primaldev.ppm.ui.task;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.activiti.engine.task.Task;
import org.primaldev.ppm.PackageprocessmanagerUI;
import org.primaldev.ppm.event.RefreshLabels_Event;
import org.primaldev.ppm.ui.identity.UserListForm;
import org.primaldev.ppm.util.ProcessUtil;

import com.github.wolfie.blackboard.Blackboard;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.Reindeer;

public class TaskAssignPopup extends PopupView {
	
	private static Logger log = Logger.getLogger(ExploreTasks.class.getName());

	public TaskAssignPopup(String small, Component layout) {
		super(small, layout);
		
	}

		
	@SuppressWarnings("serial")	
	protected PopupView createTaskPopup(final Task task, VerticalLayout layout) {
	
		layout.setSizeUndefined();
		layout.setMargin(true);
		layout.setSpacing(true);
		Label header = new Label(String.format(
				"What would you like to do with <b>%s</b>?", task.getName()));
		header.setContentMode(ContentMode.HTML);
		layout.addComponent(header);

		Button assignToMeButton = new Button("Claim the task");
		assignToMeButton.addStyleName(Reindeer.BUTTON_SMALL);
		assignToMeButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				assignTaskToCurrentUser(task);
				setPopupVisible(false);
			}
		});
		layout.addComponent(assignToMeButton);

		Button assignToOtherButton = new Button("Assign to other user...");
		assignToOtherButton.addStyleName(Reindeer.BUTTON_SMALL);
		assignToOtherButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				assignTaskToOtherUser(task);
				setPopupVisible(false);
			}
		});
		layout.addComponent(assignToOtherButton);
		
		
		Button showHistoryButton = new Button("Show History");
		showHistoryButton.addStyleName(Reindeer.BUTTON_SMALL);
		showHistoryButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				showHistory(task);
				setPopupVisible(false);
			}
		});
		layout.addComponent(showHistoryButton);
		
		return this;
	}
	
	
	private void showHistory(Task task){
		Window historyWindow = new Window("User List");
		final TaskHistoryForm historyList = new TaskHistoryForm(historyWindow, task);
		historyWindow.setContent(historyList);
		historyWindow.setSizeUndefined();
		UI.getCurrent().addWindow(historyWindow);
	}
	
	
	public void assignTaskToCurrentUser(Task task) {
		String currentUserId = ProcessUtil.getIdOfCurrentUser();

		log.log(Level.INFO, "Assigning task {1} to user {2}", new Object[] {
				task.getId(), currentUserId });
		try {
			ProcessUtil.getTaskService().claim(task.getId(), currentUserId);			
	        Blackboard bb = ((PackageprocessmanagerUI)getUI()).getBlackboard();
	        bb.fire(new RefreshLabels_Event());  
			showTaskAssignmentSuccess(task);
		} catch (ActivitiObjectNotFoundException e) {
			log.log(Level.SEVERE, "Could not assign task to user", e);
			showTaskAssignmentFailure(task);
		} catch (ActivitiTaskAlreadyClaimedException e) {
			showTaskClaimFailure(task);
		}
		
		
	}
	
	
	
	public void showTaskAssignmentSuccess(Task task) {
		Notification.show(String.format("%s assigned successfully", task.getName()),
				Notification.Type.HUMANIZED_MESSAGE);
	}

	
	public void showTaskAssignmentFailure(Task task) {
		Notification.show(
						String.format(
								"Could not assign %s. Please check the logs for more information.",
								task.getName()),
						Notification.Type.ERROR_MESSAGE);
	}
	
	
	public void showTaskClaimFailure(Task task) {
		Notification.show(
						String.format(
								"Could not claim %s. Task allready claimed.",
								task.getName()),
						Notification.Type.ERROR_MESSAGE);
	}
	
	
	public void assignTaskToOtherUser(final Task task) {
		Window userListWindow = new Window("User List");
		final UserListForm userList = new UserListForm(userListWindow);
		userListWindow.setContent(userList);
		userListWindow.setSizeUndefined();
		UI.getCurrent().addWindow(userListWindow);
			
		userListWindow.addCloseListener(new CloseListener(){
				@Override
				public void windowClose(CloseEvent e) {
					
				} 					
		});

		userListWindow.addListener(new Listener(){
			@Override
			public void componentEvent(Event event) {
				
					try {
						ProcessUtil.getTaskService().setAssignee(task.getId(), userList.getSelectedUser());
						showTaskAssignmentSuccess(task);
					} catch (Exception e) {
						showTaskAssignmentFailure(task);
					} 
						
					Blackboard bb = ((PackageprocessmanagerUI)getUI()).getBlackboard();				        
					bb.fire(new RefreshLabels_Event());
					
				
			}
			
		});
	}

	
}
