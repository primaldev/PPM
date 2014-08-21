package org.primaldev.ppm.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface LoginSuccess_Listener extends Listener {
	@ListenerMethod
	void onLoginSuccess(LoginSuccess_Event event);
}
