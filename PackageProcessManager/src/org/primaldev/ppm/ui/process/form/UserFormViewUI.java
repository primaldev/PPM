package org.primaldev.ppm.ui.process.form;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.form.BooleanFormType;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.primaldev.ppm.PackageprocessmanagerUI;
import org.primaldev.ppm.event.RefreshLabels_Event;
import org.primaldev.ppm.event.SwitchView_Event;
import org.primaldev.ppm.util.ProcessUtil;

import com.github.wolfie.blackboard.Blackboard;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;



public class UserFormViewUI extends CustomComponent {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private AbsoluteLayout mainLayout;

	@AutoGenerated
	private TextArea commentField;

	public static final String NAME = "UserFormViewUI";	

	Button submitButton;
	Button cancelButton;
	String taskId;
	private Task task;
	private ProcessDefinition processDef;
	boolean startForm;

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */	
	public UserFormViewUI(ProcessDefinition processDef) {
		this.processDef = processDef;
		startForm=true;
		buildMainLayout();
		setCompositionRoot(mainLayout);		
		autoBuildStartProcessForm();
		addClickListeners();
	}
	
	public UserFormViewUI(Task task) {
		this.task = task;
		startForm=false;
		buildMainLayout();
		setCompositionRoot(mainLayout);	
		autoBuildInProcessForm();
		addClickListeners();
	}
	

	private FormService getFormService() {
		return ProcessEngines.getDefaultProcessEngine().getFormService();
	}
	
	@SuppressWarnings("serial")
	private void addClickListeners(){
		
		submitButton.addClickListener( new Button.ClickListener()  {
		    public void buttonClick(Button.ClickEvent event) {		    	
		    	processFormValues(mainLayout);
		    }
		});
		
		cancelButton.addClickListener( new Button.ClickListener()  {
		    public void buttonClick(Button.ClickEvent event) {		    	
		   		    	//TODO: make it happen
		    }
		});		
	}
	
	

	private void autoBuildStartProcessForm(){
		taskId =processDef.getId();
		StartFormData formData = getFormService().getStartFormData(processDef.getId());
		populateStartProcessFormFields(formData);
		
	}
	
	private void autoBuildInProcessForm(){
		taskId = task.getId();
		TaskFormData form = getFormService().getTaskFormData(task.getId());
		populateInProcessFormFields(form);
		
	}
	
	

	
	private int generateForm(List<FormProperty> formProperties){
		int i=100;
		String fieldValue;
	for (final FormProperty property : formProperties) {	
		
		if (property.getValue()==null) {
			fieldValue = "";
		}else{
			fieldValue = property.getValue();
		}
		
		FormType bla = property.getType();
		
		
		
		if (property.getType()!=null && property.getType() instanceof BooleanFormType ){
			
						
			mainLayout.addComponent(new CheckBox() {{ setValue(Boolean.valueOf(property.getValue())); setCaption(property.getName()); setId(property.getId());  }}, "top:" + i + "px;left:100.0px;");
			
			
		} else {
		
			final String finalFieldValue = fieldValue;
			mainLayout.addComponent(new TextField() {{ setValue(finalFieldValue); setCaption(property.getName()); setId(property.getId()); setWidth("300px"); }}, "top:" + i + "px;left:100.0px;");
		
		}
		i=i +50;
	}
		return i;
		
	}
	
	
	protected void populateStartProcessFormFields(StartFormData formData) {
		
		int i = generateForm(formData.getFormProperties());
		addButtons(i);
		
	}
	
	protected void populateInProcessFormFields(TaskFormData form) {
		
	int i = generateForm(form.getFormProperties());
	addCommentField(i);
	i=i +200;
	addButtons(i);
	
	}
	
	
	private void addButtons(int i){
		
		// submitButton
		submitButton = new Button();
		submitButton.setCaption("Submit");
		submitButton.setImmediate(false);
		submitButton.setWidth("-1px");
		submitButton.setHeight("-1px");
		mainLayout.addComponent(submitButton, "top:" + i + "px;left:100.0px;");
		
		// cancelButton
		cancelButton = new Button();
		cancelButton.setCaption("Cancel");
		cancelButton.setImmediate(false);
		cancelButton.setWidth("-1px");
		cancelButton.setHeight("-1px");
		mainLayout.addComponent(cancelButton, "top:" + i + "px;left:300.0px;");
	}
	
	private void addCommentField(int i) {
		// commentField
		commentField = new TextArea();
		commentField.setImmediate(false);
		commentField.setWidth("400px");
		commentField.setHeight("150px");
		commentField.setCaption("Comment");
		mainLayout.addComponent(commentField,"top:" + i + "px;left:100.0px;");
	}
	
	
	public void processFormValues(ComponentContainer layout) {
		HashMap<String, String> map = new HashMap<String, String>();		
		
        Iterator<Component> componentIterator = layout.iterator();
        while (componentIterator.hasNext()) {
            Component component = componentIterator.next();
            // This would be specifically only for TextFields
            if (component instanceof TextField) {               
                map.put((String) ((TextField) component).getId(), (String) ((TextField) component).getValue());
               
            } else if (component instanceof CheckBox) {
                System.out.println("Alt 1: Checkbox boolean value: "
                        + ((CheckBox) component).getValue());            
            }           
              
        }
        
        if (startForm){
        	String userId = ProcessUtil.getIdOfCurrentUser();
        	if (userId !=null){        		
        		String businessKey = UUID.randomUUID().toString();
        		getFormService().submitStartFormData(taskId,businessKey,map); 
        		System.out.println("Process startform submitted with id: " + taskId);
        	}else{
        		Notification.show("Bad User", "Username unknown, not posting", Notification.Type.ERROR_MESSAGE);
        	}
        	
        }else{
        	String userId = ProcessUtil.getIdOfCurrentUser();
        	if (userId !=null){
        		
        		ProcessUtil.getIdentityService().setAuthenticatedUserId(userId);        	    
          	    if (commentField.getValue().length() > 0) {     
          	    	Calendar cal = Calendar.getInstance();
            		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
                	ProcessUtil.getTaskService().addComment(task.getId(), task.getProcessInstanceId(),"[" + sdf.format(cal.getTime())  + "]  " + userId + ": " + commentField.getValue());
                }
          	    getFormService().submitTaskFormData(taskId,map);
          	  
        	}else{
        		Notification.show("Bad User", "Username unknown, not posting", Notification.Type.ERROR_MESSAGE);
        	}
          
        }
        
   
        
        //fire renew event
        
        
        Blackboard bb = ((PackageprocessmanagerUI)getUI()).getBlackboard();
        bb.fire(new RefreshLabels_Event());        
    	bb.fire(new SwitchView_Event("ProcessListUI", this.NAME));	
        
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
		
		
		
		return mainLayout;
	}

}
