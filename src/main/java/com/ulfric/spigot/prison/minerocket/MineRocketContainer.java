package com.ulfric.spigot.prison.minerocket;

import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;

public class MineRocketContainer extends Container {
	
	@Initialize
	private void initialize()
	{
		this.install(MineRockets.class);
		this.install(MineRocketCommand.class);
		this.install(MineRocketGiveCommand.class);
		this.install(MineRocketListener.class);
	}
	
}
