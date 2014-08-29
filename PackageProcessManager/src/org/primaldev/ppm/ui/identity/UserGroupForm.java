package org.primaldev.ppm.ui.identity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class UserGroupForm extends CustomComponent {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private AbsoluteLayout mainLayout;

	@AutoGenerated
	private Label userCaption;

	@AutoGenerated
	private Button saveButton;

	@AutoGenerated
	private Button cancelButton;

	@AutoGenerated
	private TwinColSelect groupListSelect;

	String userName;
	Window mywindow;
	List<Group> allGroups;
	
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public UserGroupForm(Window mywindow, String userName) {
		this.userName = userName;
		this.mywindow = mywindow;
		buildMainLayout();
		setCompositionRoot(mainLayout);
		initComponents();
		addClickListeners();
		
	}
	
	private void initComponents() {		
		groupListSelect.setLeftColumnCaption("Available");
		groupListSelect.setRightColumnCaption("Assigned");
		userCaption.setValue(String.format("Groups for user %s:", userName));
		
		allGroups = getAllGroups();
		HashSet<String> userGroupNames = getUserGroupNames();	
		
		for (Group group : allGroups) {
			groupListSelect.addItem(group.getName());		
		}
		
		groupListSelect.setValue(userGroupNames);
		
	}
	
	private String getGroupID(String groupname){		
		
		for (Group group: allGroups) {			
			if (group.getName().equals(groupname)) {				
				return group.getId();	
				
			}		
		}		
			return "";	
	}
	
private void addClickListeners(){
		
	cancelButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().removeWindow(mywindow);
			}
		});
	
	saveButton.addClickListener(new Button.ClickListener() {
		public void buttonClick(ClickEvent event) {
			saveUserGroup();
		}
	});
	
	
	
}
	
private void saveUserGroup(){
		Set<String> newGroupList =  (Set<String>) groupListSelect.getValue();
		
		HashSet<String> needRemoval = getUserGroupNames();
	//what needs to be removed
		needRemoval.removeAll(newGroupList);
	
	//what needs to be added
		HashSet<String> currenList = getUserGroupNames();
		if (currenList.size() > 0) {
			newGroupList.removeAll(currenList);
		}
		
		if (needRemoval.size() > 0 ) {
			for (String groupname : needRemoval) {
				getIdentityService().deleteMembership(userName, getGroupID(groupname));
			}
		}
		
		if (newGroupList.size() > 0 ) {
			for (String groupname : newGroupList){
				getIdentityService().createMembership(userName, getGroupID(groupname));
			}
		}
	
		UI.getCurrent().removeWindow(mywindow);
		Notification.show("Group Form",
                "Groupmembership modified...",
                Notification.Type.TRAY_NOTIFICATION);
		
		
}

	
	private List<Group> getAllGroups() {
		GroupQuery query = getIdentityService().createGroupQuery();		
		return query.orderByGroupId().asc().list();		
	}	
	
	private HashSet<String> getUserGroupNames() {
		GroupQuery query = getIdentityService().createGroupQuery();
		List<Group> groupList = query.groupMember(userName).list();
		HashSet<String> groupIdSet = new HashSet<String>();
		for (Group group : groupList) {
			groupIdSet.add(group.getName());
		}
		return groupIdSet;
	}
	
	private IdentityService getIdentityService() {
		return ProcessEngines.getDefaultProcessEngine().getIdentityService();
	}

	@AutoGenerated
	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("520px");
		mainLayout.setHeight("420px");
		
		// top-level component properties
		setWidth("520px");
		setHeight("420px");
		
		// groupListSelect
		groupListSelect = new TwinColSelect();
		groupListSelect.setImmediate(false);
		groupListSelect.setWidth("440px");
		groupListSelect.setHeight("280px");
		mainLayout.addComponent(groupListSelect, "top:60.0px;left:40.0px;");
		
		// cancelButton
		cancelButton = new Button();
		cancelButton.setCaption("Cancel");
		cancelButton.setImmediate(true);
		cancelButton.setWidth("-1px");
		cancelButton.setHeight("-1px");
		mainLayout.addComponent(cancelButton, "top:360.0px;left:340.0px;");
		
		// saveButton
		saveButton = new Button();
		saveButton.setCaption("Save");
		saveButton.setImmediate(true);
		saveButton.setWidth("-1px");
		saveButton.setHeight("-1px");
		mainLayout.addComponent(saveButton, "top:360.0px;left:90.0px;");
		
		// userCaption
		userCaption = new Label();
		userCaption.setImmediate(false);
		userCaption.setWidth("300px");
		userCaption.setHeight("-1px");
		userCaption.setValue("Label");
		mainLayout.addComponent(userCaption, "top:20.0px;left:120.0px;");
		
		return mainLayout;
	}

}
