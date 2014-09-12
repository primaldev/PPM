package org.primaldev.ppm.ui.main;


import com.vaadin.ui.Tree;

/*
 * 
 * Just a seperate class to build the menu. MainUI is getting to big.
 * 
 */

public class MainMenu {
	Tree tree;
	
	//For translation later on
	
	public static final String HOME = "Home";
	public static final String MY_TASKS = "My Tasks";
	public static final String AVAILABLE_TASKS = "Available Tasks";
	public static final String EXPLORE_TASKS = "Task Explorer";
	public static final String COMPLETED_TASKS = "Completed Tasks";
	public static final String START_TASK = "Start Task";
	public static final String MANAGE = "Manage";
	public static final String PROCESSES = "Processes";
	public static final String IDENTITY = "Identity";
	public static final String SITE_ADMIN = "Site Admin";
	public static final String MODULES = "Modules";
	
	
	public MainMenu(Tree tree) {
		this.tree = tree;
		setMenu();
		
	}
	
	
	private void setMenu(){

			tree.removeAllItems();
		
			final String[][] userItems = new String[][]{
					 new String[]{HOME, MY_TASKS,AVAILABLE_TASKS, EXPLORE_TASKS,START_TASK,COMPLETED_TASKS }
			};
			
			buildMenu(userItems);
			
			//if manager role
			final String[][] managerItems =new String[][]{ 
					new String[]{MANAGE, PROCESSES,IDENTITY }
			};
			buildMenu(managerItems);
			
			
			//if admin role
			final String[][] adminItems =new String[][]{ 
					new String[]{SITE_ADMIN, MODULES}
			};
			buildMenu(adminItems);		
		
		
	}
	
	private void buildMenu(String[][] menuItems) {
		for (int i=0; i<menuItems.length; i++) {
		    String planet = (menuItems[i][0]);
		    tree.addItem(planet);
		    
		    if (menuItems[i].length == 1) {
		        // The planet has no moons so make it a leaf.
		        tree.setChildrenAllowed(planet, false);
		    } else {
		        // Add children (moons) under the planets.
		        for (int j=1; j<menuItems[i].length; j++) {
		            String moon = menuItems[i][j];
		            
		            // Add the item as a regular item.
		            tree.addItem(moon);
		            
		            // Set it to be a child.
		            tree.setParent(moon, planet);
		            
		            // Make the moons look like leaves.
		            tree.setChildrenAllowed(moon, false);
		        }

		        // Expand the subtree.
		        tree.expandItemsRecursively(planet);
		    }
		}	
	
	}

}
