package org.primaldev.ppm.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface RefreshLabels_Listener extends Listener{
	@ListenerMethod
	void onRefreshLabels(RefreshLabels_Event event);
}
