package com.ulfric.spigot.prison.essentials.repair;

import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;

public class RepairContainer extends Container {
	
	@Initialize
	private void initialize()
	{
		this.install(RepairService.class);
		this.install(RepairCommand.class);
		this.install(RepairHandCommand.class);
		this.install(RepairAllCommand.class);
	}
	
}
