package com.ulfric.spigot.prison.command;

import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;

public class EssentialCommandsContainer extends Container {

	@Initialize
	private void initialize()
	{
		this.install(CommandFeed.class);
		this.install(CommandCraft.class);
		this.install(CommandEnderchest.class);
		this.install(CommandFly.class);
		this.install(CommandDay.class);
		this.install(CommandNight.class);
		this.install(CommandNightVision.class);
		this.install(CommandSun.class);
		this.install(CommandFix.class);
		this.install(CommandFixHand.class);
		this.install(CommandFixAll.class);
		this.install(CommandBlocks.class);
		this.install(CommandClearInventory.class);
	}

}