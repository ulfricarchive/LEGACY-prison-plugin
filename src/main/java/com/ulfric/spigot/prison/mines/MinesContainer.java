package com.ulfric.spigot.prison.mines;

import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;

public class MinesContainer extends Container {

	@Initialize
	private void initialize()
	{
		this.install(MinesService.class);
	}

}
