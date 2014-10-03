package org.primaldev.ppm;

import javax.servlet.annotation.WebServlet;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.primaldev.ppm.event.RefreshLabels_Event;
import org.primaldev.ppm.event.RefreshLabels_Listener;
import org.primaldev.ppm.event.SwitchView_Event;
import org.primaldev.ppm.event.SwitchView_Listener;
import org.primaldev.ppm.ui.main.MainUI;
import org.primaldev.ppm.util.OnEnterKeyHandler;

import com.github.wolfie.blackboard.Blackboard;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.Connect;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
@Theme("packageprocessmanager")
@PreserveOnRefresh
public class PackageprocessmanagerUI extends UI  {

	MainUI mainUI;
	
	@AutoGenerated
	private AbsoluteLayout mainLayout;
	@AutoGenerated
	private Button loginButton;
	@AutoGenerated
	private PasswordField passWord;
	@AutoGenerated
	private TextField userName;
	
	//event bus
	private transient Blackboard blackboard = new Blackboard();

	//  private static ThreadLocal<Blackboard> BLACKBOARD = new ThreadLocal<Blackboard>();
	 // private final Blackboard blackboardInstance = new Blackboard();
	
	
	//Check the Error log for output (Show view -> error.log) because the widget compiler uses the wrong path 
	
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = PackageprocessmanagerUI.class, widgetset="org.primaldev.ppm.ui.main.PackageprocessmanagerWidgetset")	
    
	public static class Servlet extends VaadinServlet {
		
	}

	@Override	
	protected void init(VaadinRequest request) {
		//BLACKBOARD.set(blackboardInstance);
		
		
		buildMainLayout();
		addClicklisteners();
		setContent(mainLayout);		 
		
		userName.setValue("admin");
		passWord.setValue("password");
		
		
		blackboard.register(SwitchView_Listener.class, SwitchView_Event.class);
		blackboard.register(RefreshLabels_Listener.class, RefreshLabels_Event.class);
	
		
		if (isUserloggedin()) {
			
			setMainUI();
		} else {
			
			setContent(mainLayout);
		}
		


		
		
		/*Button button = new Button("Click Me");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				layout.addComponent(new Label("Thank you for clicking"));
			}
		});
		layout.addComponent(button);
		*/
	}
	
	private void setMainUI(){
		mainUI = new MainUI();
		getBlackboard().addListener(mainUI);
		setContent(mainUI);
		
	}
	

	
	private boolean isUserloggedin(){
		try {
			if (getSession().getAttribute("myBite") == null) {
				
					return false;	
				
			}else{
					return true;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
		}		
		
		
		return false;
	}
	
  
	public  Blackboard getBlackboard() {
		    return blackboard;
	}
	
	private void loginUser(){
		if (getIdentityService().checkPassword(userName.getValue(), passWord.getValue())) {
			getIdentityService().setAuthenticatedUserId(userName.getValue());					
			
			try {
			    VaadinSession.getCurrent().getLockInstance().lock();
			    VaadinSession.getCurrent().setAttribute("myBite", userName.getValue());
			} finally {
			    VaadinSession.getCurrent().getLockInstance().unlock();
			}					
			
			setMainUI();					
			
		} else {
			
			clearForm();
		    showLoginFailed();
		}
	}
	
	@SuppressWarnings("serial")
	private void addClicklisteners() {
		loginButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				loginUser();
			}
		});
		
		OnEnterKeyHandler onEnterHandler=new OnEnterKeyHandler(){
            @Override
            public void onEnterKeyPressed() {
            	loginUser();
            }
        };
        
        
        onEnterHandler.installOn(userName);
        onEnterHandler.installOn(passWord);
        
	}
	
	private IdentityService getIdentityService() {
		return ProcessEngines.getDefaultProcessEngine().getIdentityService();
	}
	
	
	public void showLoginFailed() {
		
		Notification.show("Login Status",
                "Login failed. Please try again.",
                Notification.Type.TRAY_NOTIFICATION);
	}
	
	public void showLoginGood() {
		
		Notification.show("Login Successfull",
                "Welcome...",
                Notification.Type.TRAY_NOTIFICATION);
	}


	public void clearForm() {
		userName.setValue("");
		passWord.setValue("");
		userName.focus();
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
		
		VerticalLayout loginPanel = new VerticalLayout();
		loginPanel.setSpacing(true);
		loginPanel.setWidth("300px");

		Label header = new Label("Please login");
		header.addStyleName(Reindeer.LABEL_H1);
		loginPanel.addComponent(header);		
		
		// userName
		userName = new TextField();
		userName.setCaption("User Name");
		userName.setImmediate(false);
		userName.setWidth("100%");
		userName.setHeight("-1px");
		loginPanel.addComponent(userName);
		
		// passWord
		passWord = new PasswordField();
		passWord.setCaption("Password");
		passWord.setImmediate(false);
		passWord.setWidth("100%");
		passWord.setHeight("-1px");
		loginPanel.addComponent(passWord);
		
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(true);
		loginPanel.addComponent(buttons);
		loginPanel.setComponentAlignment(buttons, Alignment.MIDDLE_RIGHT);
		
		
		// loginButton
		loginButton = new Button();
		loginButton.setCaption("Login");
		loginButton.setImmediate(true);
		loginButton.setWidth("-1px");
		loginButton.setHeight("-1px");
		buttons.addComponent(loginButton);
		
		mainLayout.addComponent(loginPanel,"top:30%;left:40%;");
		
		mainLayout.setSizeFull();
		mainLayout.addStyleName(Reindeer.LAYOUT_BLACK);

		
		return mainLayout;
	}

}