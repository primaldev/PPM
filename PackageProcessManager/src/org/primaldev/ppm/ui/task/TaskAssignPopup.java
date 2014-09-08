package org.primaldev.ppm.ui.task;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.activiti.engine.task.Task;
import org.primaldev.ppm.PackageprocessmanagerUI;
import org.primaldev.ppm.event.RefreshLabels_Event;
import org.primaldev.ppm.util.ProcessUtil;

import com.github.wolfie.blackboard.Blackboard;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
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

		Button assignToMeButton = new Button("Assign to me");
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
		// TODO Add listener
		layout.addComponent(assignToOtherButton);
		return this;
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
		} catch (RuntimeException e) {
			log.log(Level.SEVERE, "Could not assign task to user", e);
			showTaskAssignmentFailure(task);
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
	
	public void assignTaskToOtherUser(Task task) {
		// TODO Implement me!
	}

	
}
