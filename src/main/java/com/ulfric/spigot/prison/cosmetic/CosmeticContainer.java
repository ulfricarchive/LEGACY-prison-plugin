package com.ulfric.spigot.prison.cosmetic;

import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;

public class CosmeticContainer extends Container {
	
	@Initialize
	private void initialize()
	{
		this.install(CosmeticService.class);
	}
	
}
