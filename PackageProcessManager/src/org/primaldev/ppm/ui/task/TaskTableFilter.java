package org.primaldev.ppm.ui.task;

import java.util.regex.PatternSyntaxException;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Label;

public class TaskTableFilter implements Container.Filter{
	  protected String propertyId;
	    protected String regex;
	    protected Label  status;
	    
	    public TaskTableFilter(String propertyId, String regex, Label status) {
	        this.propertyId = propertyId;
	        this.regex      = regex;
	        this.status     = status;
	    }

	    /** Apply the filter on an item to check if it passes. */
	    @Override
	    public boolean passesFilter(Object itemId, Item item)
	            throws UnsupportedOperationException {
	        // Acquire the relevant property from the item object
	        Property p = item.getItemProperty(propertyId);
	        
	        // Should always check validity
	        if (p == null || !p.getType().equals(String.class))
	            return false;
	        String value = (String) p.getValue();
	        
	        // Pass all if regex not given
	        if (regex.isEmpty()) {
	            status.setValue("Empty filter");
	            return true;
	        }
	        
	        // The actual filter logic + error handling
	        try {
	            boolean result = value.matches(regex);
	            status.setValue(""); // OK
	            return result;
	        } catch (PatternSyntaxException e) {
	            status.setValue("Invalid pattern");
	            return false;
	        }
	    }

	    /** Tells if this filter works on the given property. */
	    @Override
	    public boolean appliesToProperty(Object propertyId) {
	        return propertyId != null &&
	               propertyId.equals(this.propertyId);
	    }
	
	
}
