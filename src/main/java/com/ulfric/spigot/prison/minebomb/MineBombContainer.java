package com.ulfric.spigot.prison.minebomb;

import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;

public class MineBombContainer extends Container {
	
	@Initialize
	private void initialize()
	{
		if (true)
		{
			throw new UnsupportedOperationException("Refactor Listener, add permission to give, cache stuff in listener, call artificial block breaks");
		}
		this.install(MineBombs.class);
		this.install(MineBombCommand.class);
		this.install(MineBombGiveCommand.class);
		this.install(MineBombListener.class);
	}
	
}
