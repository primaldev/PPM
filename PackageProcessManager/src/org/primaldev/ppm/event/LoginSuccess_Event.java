package org.primaldev.ppm.event;

import com.github.wolfie.blackboard.Event;

public class LoginSuccess_Event implements Event{
	private String user = null;

	public LoginSuccess_Event(String u) {
		user = u;
	}

	public String getUser() {
		return user;
	}
}
