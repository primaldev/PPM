package org.primaldev.ppm;

import javax.servlet.annotation.WebServlet;

import org.primaldev.ppm.ui.login.LoginUI;
import org.primaldev.ppm.ui.main.MainUI;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("packageprocessmanager")
public class PackageprocessmanagerUI extends UI {
	Navigator navigator;
	protected static final String MAINVIEW = "main";
	
	//private LoginUI loginUI = new LoginUI();
	
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = PackageprocessmanagerUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		//final VerticalLayout layout = new VerticalLayout();
		navigator = new Navigator(this, this);
		//layout.setMargin(true);
		
		//setContent(layout);
	//	setSizeFull();
		//setContent(loginUI);
		
		
		
		navigator.addView("", new LoginUI(navigator)); //default fallback view required
		navigator.addView(MAINVIEW, new MainUI());
		
		//LoginUI loginUI = new LoginUI();
		//layout.addComponent(loginUI);
		//setContent(loginUI);

		/* Button button = new Button("Click Me");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				layout.addComponent(new Label("Thank you for clicking"));
			}
		});
		layout.addComponent(button);
		*/
	}

}