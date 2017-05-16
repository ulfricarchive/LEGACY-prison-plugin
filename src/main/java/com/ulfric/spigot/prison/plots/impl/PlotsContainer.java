package com.ulfric.spigot.prison.plots.impl;

import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;

public class PlotsContainer extends Container {

	@Initialize
	private void initialize()
	{
		this.install(PlotsService.class);
	}

}
