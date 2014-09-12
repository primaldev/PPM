package org.primaldev.ppm.ui.main;


import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.primaldev.ppm.event.RefreshLabels_Event;
import org.primaldev.ppm.event.RefreshLabels_Listener;
import org.primaldev.ppm.event.SwitchView_Event;
import org.primaldev.ppm.event.SwitchView_Listener;
import org.primaldev.ppm.ui.identity.IdentityManagementUI;
import org.primaldev.ppm.ui.process.ProcessListUI;
import org.primaldev.ppm.ui.process.form.UserFormViewUI;
import org.primaldev.ppm.ui.task.ExploreTasks;
import org.primaldev.ppm.ui.task.UserTaskList;
import org.primaldev.ppm.ui.task.history.TaskHistory;
import org.primaldev.ppm.util.ProcessUtil;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Tree;
import com.vaadin.ui.themes.Reindeer;

public class MainUI extends CustomComponent implements View, SwitchView_Listener, RefreshLabels_Listener  {

	@AutoGenerated
	private AbsoluteLayout mainLayout;

	@AutoGenerated
	private AbsoluteLayout menuLeftLayout;

	@AutoGenerated
	private Tree menuTree;

	@AutoGenerated
	private TabSheet mainTabSheet;

	@AutoGenerated
	private UserTaskList userTaskList_1;

	@AutoGenerated
	private WelcomeSummaryUI welcomeSummaryUI_1;

	@AutoGenerated
	private HorizontalLayout horizontalLayout_1;

	@AutoGenerated
	private HorizontalLayout horizontalLayout_2;

	@AutoGenerated
	private Button logout;

	@AutoGenerated
	private Button availableTasks;

	@AutoGenerated
	private Button userTask;

	@AutoGenerated
	private Label label_1;

	IdentityManagementUI IdentityManagementUI_1;
	ProcessListUI processListUI;
	UserFormViewUI userFormViewUI;
	ExploreTasks unassignedTasksUI;
	ExploreTasks exploreTasks;
	TaskHistory taskHistory;
	
	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */
	
	
	
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.*/
	
	
	
	public MainUI() {
		checkSessionSecure();		
		AbsoluteLayout mainLayout = buildMainLayout();		
		setCompositionRoot(mainLayout);
		setTheme();
		addClicklisteners();		
		setLabels();
		setMenu();
		
	}

	
	private void setTheme(){
		label_1.setStyleName(Reindeer.LABEL_H1);
		mainLayout.setStyleName(Reindeer.LAYOUT_WHITE);
		horizontalLayout_1.setStyleName(Reindeer.LAYOUT_BLACK);
		horizontalLayout_2.setStyleName(Reindeer.LAYOUT_BLACK);
		mainTabSheet.setStyleName(Reindeer.TABSHEET_MINIMAL);
		welcomeSummaryUI_1.setStyleName(Reindeer.LAYOUT_BLUE);
		menuTree.setStyleName(Reindeer.TREE_CONNECTORS);
		menuLeftLayout.setStyleName(Reindeer.LAYOUT_BLUE);
	}
	
	
	private void setMenu(){
		new MainMenu(menuTree);
	}
	
	private void callMenuItem(){
		if (menuTree.getValue().equals(MainMenu.HOME)) {
			mainTabSheet.setSelectedTab(welcomeSummaryUI_1);
		}else if (menuTree.getValue().equals(MainMenu.MY_TASKS)) {
			mainTabSheet.setSelectedTab(userTaskList_1);
		}else if (menuTree.getValue().equals(MainMenu.AVAILABLE_TASKS)) {
			initunAssignedTasksUI();
		}else if (menuTree.getValue().equals(MainMenu.COMPLETED_TASKS)) {
			initTaskHistory();
		}else if (menuTree.getValue().equals(MainMenu.EXPLORE_TASKS)) {
			initExploreTasks();
		}else if (menuTree.getValue().equals(MainMenu.IDENTITY)) {
			initIdentityTab();
		}else if (menuTree.getValue().equals(MainMenu.START_TASK)) {
			initProcessList();
		}
		
		
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		setLabels();		
	}

	
	private void checkSessionSecure() {
	     if (ProcessUtil.getIdOfCurrentUser()==null) {
				Notification.show("Bad User",
			            "You should not be here",
			            Notification.Type.TRAY_NOTIFICATION);						
				badUser();
				
			}else{
				Notification.show("Welcome",
						ProcessUtil.getIdOfCurrentUser(),
			            Notification.Type.HUMANIZED_MESSAGE);
			}
	}


	private void badUser(){
		getIdentityService().setAuthenticatedUserId(null);		
		VaadinSession.getCurrent().setAttribute("myBite", null);
		VaadinSession.getCurrent().close();
		//getUI().getPage().setLocation("");		
	}
	

	
	@SuppressWarnings("serial")
	private void addClicklisteners() {
		logout.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
		
					getIdentityService().setAuthenticatedUserId(null);
					getSession().setAttribute("user", null);
					getSession().close();
					//Blackboard bb = PackageprocessmanagerUI.getBlackboard();					
					getUI().getPage().setLocation("");				
			}
		});
		
		availableTasks.addClickListener(new Button.ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				initunAssignedTasksUI();
				
			}
		});
		
		
		userTask.addClickListener(new Button.ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				userTaskList_1.updateTaskList();
				mainTabSheet.setSelectedTab(userTaskList_1);
				
				
			}
		});
		
		
		menuTree.addValueChangeListener(new ValueChangeListener(){

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (menuTree.getValue()!=null) {
					callMenuItem();				
				}
			}
			
		});
		
	}
	

	
	public void setLabels(){
		
		availableTasks.setImmediate(true);
		userTask.setCaption("My Tasks (" + getNumberOfTasksAssignedToCurrentUser()  + ")");
		availableTasks.setCaption("Available Tasks ("+ getNumberOfUnassignedTasks() + ")");
		
	}
	
	private IdentityService getIdentityService() {
		return ProcessEngines.getDefaultProcessEngine().getIdentityService();
	}
	
	private long getNumberOfTasksAssignedToCurrentUser() {
		String currentUser = ProcessUtil.getIdOfCurrentUser();
		TaskQuery query = ProcessUtil.getTaskService().createTaskQuery();
		return query.taskAssignee(currentUser).count();
	}
	
	private long getNumberOfUnassignedTasks() {
		String currentUser = ProcessUtil.getIdOfCurrentUser();
		TaskQuery query = ProcessUtil.getTaskService().createTaskQuery();
		return query.taskUnassigned().taskCandidateUser(currentUser).count();
	}
	



	
	private void initIdentityTab(){
		// userTaskList_1	
		mainTabSheet.removeComponent(IdentityManagementUI_1);
		IdentityManagementUI_1 = new IdentityManagementUI();
		IdentityManagementUI_1.setImmediate(false);
		IdentityManagementUI_1.setWidth("100.0%");
		IdentityManagementUI_1.setHeight("100.0%");
		
		
			mainTabSheet.addTab(IdentityManagementUI_1, "Identity Management", null);
			mainTabSheet.getTab(IdentityManagementUI_1).setClosable(true);
			mainTabSheet.setSelectedTab(IdentityManagementUI_1);
	
	}
	
	
	private void initProcessList(){
		mainTabSheet.removeComponent(processListUI);
		processListUI = new ProcessListUI();
		processListUI.setImmediate(false);
		processListUI.setWidth("100.0%");
		processListUI.setHeight("100.0%");
		
		
			mainTabSheet.addTab(processListUI, "Processes", null);
			mainTabSheet.getTab(processListUI).setClosable(true);
			mainTabSheet.setSelectedTab(processListUI);
		
	}
	
	private void initUserFormViewUI(){
		mainTabSheet.removeComponent(userFormViewUI);
		
		userFormViewUI.setImmediate(false);
		userFormViewUI.setWidth("100.0%");
		userFormViewUI.setHeight("100.0%");		
		
			mainTabSheet.addTab(userFormViewUI, "Process Form", null);
			mainTabSheet.getTab(userFormViewUI).setClosable(true);
			mainTabSheet.setSelectedTab(userFormViewUI);
		
	}
	
	private void goUserFormViewUI(ProcessDefinition processDefinition){
		userFormViewUI = new UserFormViewUI(processDefinition);
		initUserFormViewUI();
	}
	
	private void goUserFormViewUI(Task task){
		userFormViewUI = new UserFormViewUI(task);
		initUserFormViewUI();
	}
	
	private void closeUserFormViewUI(){
		mainTabSheet.removeComponent(userFormViewUI);
	}
	
	private void initunAssignedTasksUI(){
		mainTabSheet.removeComponent(unassignedTasksUI);
		unassignedTasksUI = new ExploreTasks("Unassigned");
		unassignedTasksUI.setImmediate(false);
		unassignedTasksUI.setWidth("100.0%");
		unassignedTasksUI.setHeight("100.0%");
		
		
			mainTabSheet.addTab(unassignedTasksUI, "Unassigned Tasks", null);
			mainTabSheet.getTab(unassignedTasksUI).setClosable(true);
			mainTabSheet.setSelectedTab(unassignedTasksUI);	
					
	}
	
	

	private void initExploreTasks(){
		mainTabSheet.removeComponent(exploreTasks);
		exploreTasks = new ExploreTasks();
		exploreTasks.setImmediate(false);
		exploreTasks.setWidth("100.0%");
		exploreTasks.setHeight("100.0%");		
		
			mainTabSheet.addTab(exploreTasks, "Task Explorer", null);
			mainTabSheet.getTab(exploreTasks).setClosable(true);
			mainTabSheet.setSelectedTab(exploreTasks);
		
	}
	
	
	private void initTaskHistory(){
		
		mainTabSheet.removeComponent(taskHistory);
		taskHistory = new TaskHistory();
		taskHistory.setImmediate(false);
		taskHistory.setWidth("100.0%");
		taskHistory.setHeight("100.0%");		
		
			mainTabSheet.addTab(taskHistory, "Task History", null);
			mainTabSheet.getTab(taskHistory).setClosable(true);
			mainTabSheet.setSelectedTab(taskHistory);
		
	}
	
	@Override
	public void onSwitchView(SwitchView_Event event) {
		String viewName = event.getViewName();
		
		
		if(viewName.equals(IdentityManagementUI.NAME)){			
			initIdentityTab();
		}		
		if(viewName.equals(ProcessListUI.NAME)){			
			initProcessList();
		}
		
		if(viewName.equals(ExploreTasks.NAME)){			
			initExploreTasks();
		}
		
		if(viewName.equals(TaskHistory.NAME)){			
			initTaskHistory();
		}
		
		if(viewName.equals(UserFormViewUI.NAME)){
			if (event.isProcType()){
				goUserFormViewUI(event.getProcessDefinition());
			}else{
				goUserFormViewUI(event.getTask());
			}
		}
			
		
		if(event.isCloseView()){
			String closeViewName = event.getCloseView();
			if(closeViewName.equals(UserFormViewUI.NAME)){	
				closeUserFormViewUI();
			}
			
			
		}
		
	}
	
	@Override
	public void onRefreshLabels(RefreshLabels_Event event){
		
		setLabels();
		userTaskList_1.updateTaskList();
		
		if (unassignedTasksUI!=null) {
			unassignedTasksUI.updateTaskList();
		}
		
		if (exploreTasks!=null) {
			exploreTasks.updateTaskList();
		}
		
	}
	
	

	@AutoGenerated
	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// horizontalLayout_1
		horizontalLayout_1 = buildHorizontalLayout_1();
		mainLayout.addComponent(horizontalLayout_1,
				"top:0.0px;right:114.0px;left:101.0px;");
		
		// mainTabSheet
		mainTabSheet = buildMainTabSheet();
		mainLayout.addComponent(mainTabSheet,
				"top:100.0px;right:114.0px;bottom:40.0px;left:240.0px;");
		
		// menuLeftLayout
		menuLeftLayout = buildMenuLeftLayout();
		mainLayout.addComponent(menuLeftLayout,
				"top:100.0px;bottom:40.0px;left:101.0px;");
		
		return mainLayout;
	}


	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout_1() {
		// common part: create layout
		horizontalLayout_1 = new HorizontalLayout();
		horizontalLayout_1.setImmediate(false);
		horizontalLayout_1.setWidth("100.0%");
		horizontalLayout_1.setHeight("100px");
		horizontalLayout_1.setMargin(false);
		horizontalLayout_1.setSpacing(true);
		
		// label_1
		label_1 = new Label();
		label_1.setImmediate(false);
		label_1.setWidth("-1px");
		label_1.setHeight("40px");
		label_1.setValue("Process Manager");
		horizontalLayout_1.addComponent(label_1);
		horizontalLayout_1.setComponentAlignment(label_1, new Alignment(48));
		
		// horizontalLayout_2
		horizontalLayout_2 = buildHorizontalLayout_2();
		horizontalLayout_1.addComponent(horizontalLayout_2);
		horizontalLayout_1.setComponentAlignment(horizontalLayout_2,
				new Alignment(48));
		
		return horizontalLayout_1;
	}


	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout_2() {
		// common part: create layout
		horizontalLayout_2 = new HorizontalLayout();
		horizontalLayout_2.setImmediate(false);
		horizontalLayout_2.setWidth("495px");
		horizontalLayout_2.setHeight("100px");
		horizontalLayout_2.setMargin(false);
		
		// userTask
		userTask = new Button();
		userTask.setCaption("My Tasks (0)");
		userTask.setImmediate(true);
		userTask.setWidth("-1px");
		userTask.setHeight("-1px");
		horizontalLayout_2.addComponent(userTask);
		horizontalLayout_2.setComponentAlignment(userTask, new Alignment(34));
		
		// availableTasks
		availableTasks = new Button();
		availableTasks.setCaption("Available Tasks (0)");
		availableTasks.setImmediate(true);
		availableTasks.setWidth("-1px");
		availableTasks.setHeight("-1px");
		horizontalLayout_2.addComponent(availableTasks);
		horizontalLayout_2.setComponentAlignment(availableTasks, new Alignment(
				33));
		
		// logout
		logout = new Button();
		logout.setCaption("Logout");
		logout.setImmediate(true);
		logout.setWidth("-1px");
		logout.setHeight("-1px");
		horizontalLayout_2.addComponent(logout);
		horizontalLayout_2.setComponentAlignment(logout, new Alignment(48));
		
		return horizontalLayout_2;
	}


	@AutoGenerated
	private TabSheet buildMainTabSheet() {
		// common part: create layout
		mainTabSheet = new TabSheet();
		mainTabSheet.setImmediate(true);
		mainTabSheet.setWidth("100.0%");
		mainTabSheet.setHeight("100.0%");
		
		// welcomeSummaryUI_1
		welcomeSummaryUI_1 = new WelcomeSummaryUI();
		welcomeSummaryUI_1.setImmediate(false);
		welcomeSummaryUI_1.setWidth("100.0%");
		welcomeSummaryUI_1.setHeight("100.0%");
		mainTabSheet.addTab(welcomeSummaryUI_1, "Welcome", null);
		
		// userTaskList_1
		userTaskList_1 = new UserTaskList();
		userTaskList_1.setImmediate(false);
		userTaskList_1.setWidth("100.0%");
		userTaskList_1.setHeight("100.0%");
		mainTabSheet.addTab(userTaskList_1, "My Tasks", null);
		
		return mainTabSheet;
	}


	@AutoGenerated
	private AbsoluteLayout buildMenuLeftLayout() {
		// common part: create layout
		menuLeftLayout = new AbsoluteLayout();
		menuLeftLayout.setImmediate(false);
		menuLeftLayout.setWidth("139px");
		menuLeftLayout.setHeight("100.0%");
		
		// menuTree
		menuTree = new Tree();
		menuTree.setImmediate(true);
		menuTree.setWidth("140px");
		menuTree.setHeight("100.0%");
		menuLeftLayout.addComponent(menuTree,
				"top:0.0px;bottom:40.0px;left:0.0px;");
		
		return menuLeftLayout;
	}


}
