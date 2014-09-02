package org.primaldev.ppm.ui.process.form;

import java.util.HashMap;
import java.util.Iterator;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.ProcessDefinition;
import org.primaldev.ppm.PackageprocessmanagerUI;
import org.primaldev.ppm.event.RefreshLabels_Event;
import org.primaldev.ppm.event.SwitchView_Event;

import com.github.wolfie.blackboard.Blackboard;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TextField;



public class UserFormViewUI extends CustomComponent {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private AbsoluteLayout mainLayout;

	public static final String NAME = "UserFormViewUI";	
	ProcessDefinition processDef;
	Button submitButton;
	Button cancelButton;
	boolean startForm;	
	StartFormData formData;

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */	
	public UserFormViewUI(ProcessDefinition processDef) {
		this.processDef=processDef; 
		buildMainLayout();
		setCompositionRoot(mainLayout);
		
		
		if (isStartForm()){
			startForm=true;			
			autoBuildForm();
		} else if (isTaskForm()){
			startForm=false;			
			autoBuildForm();
		} else {
			startForm=false;
			
		}
		
		addClickListeners();
	}
	
	
	private boolean isTaskForm(){
		if ( getFormKey(processDef)!= null) {
			return true;
		}else{
			return false;
		}
	}
	
	private boolean isStartForm(){
		if ( processDef.getId()!= null) {
			return true;
		}else{
			return false;
		}
	}
	
	private String getFormKey(ProcessDefinition processDef) {
		return getFormService().getStartFormData(processDef.getId())
				.getFormKey();
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
		   		    	
		    }
		});		
	}
	
	

	private void autoBuildForm(){
		
		formData = getFormService().getStartFormData(processDef.getId());
		populateFormFields();
		
	}
	
	protected void populateFormFields() {
			int i=100;
		
		for (final FormProperty property : formData.getFormProperties()) {			
			mainLayout.addComponent(new TextField() {{ setValue(property.getValue()); setCaption(property.getName()); setId(property.getId()); setWidth("300px"); }}, "top:" + i + "px;left:100.0px;");
			i=i +50;
		}
		
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
        	getFormService().submitStartFormData(formData.getProcessDefinition().getId(),map); 
        	System.out.println("Process startform submitted with id: " + formData.getProcessDefinition().getId());
        }else{
        	getFormService().submitTaskFormData(formData.getProcessDefinition().getId(),map); 
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