package com.ulfric.spigot.prison.essentials;

import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;

public class EssentialsContainer extends Container {

	@Initialize
	private void initialize()
	{
		this.install(FeedCommand.class);
		this.install(CraftCommand.class);
		this.install(EnderchestCommand.class);
		this.install(FlightCommand.class);
		this.install(DayCommand.class);
		this.install(NightCommand.class);
		this.install(NightVisionCommand.class);
		this.install(SunCommand.class);
		this.install(BlocksCommand.class);
		this.install(ClearInventoryCommand.class);
		this.install(WorldCommand.class);
		this.install(WorldCreateCommand.class);
	}

}