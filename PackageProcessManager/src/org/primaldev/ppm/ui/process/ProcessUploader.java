package org.primaldev.ppm.ui.process;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload.Receiver;

public class ProcessUploader implements Receiver{
	
	public File file;


	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		 FileOutputStream fos = null; // Stream to write to
	        try {
	            // Open the file for writing.
	        	
	        	File f = new File(System.getProperty("user.home") + "/.ppm/");
	        	if (!f.exists()) {
	        		f.mkdir();
	        	}	        	
	        	
	            file = new File(System.getProperty("user.home") + "/.ppm/" + filename);
	            fos = new FileOutputStream(file);
	        } catch (final java.io.FileNotFoundException e) {
	            new Notification("Could not open file<br/>",
	                             e.getMessage(),
	                             Notification.Type.ERROR_MESSAGE)
	                .show(Page.getCurrent());
	            return null;
	        }
	        return fos;
		
	}
	
	public File getFile() {
		return file;
	}

}
