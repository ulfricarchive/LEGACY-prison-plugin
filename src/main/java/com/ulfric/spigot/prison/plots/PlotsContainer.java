package com.ulfric.spigot.prison.plots;

import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;

public class PlotsContainer extends Container {

	@Initialize
	private void initialize()
	{
		this.install(PlotsService.class);
		PlotsService.getService().loadPlots();
	}

	@Override
	public void onDisable()
	{
		PlotsService.getService().savePlots();
	}
}
