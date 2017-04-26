package com.ulfric.spigot.prison.mines;

import com.ulfric.dragoon.ObjectFactory;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;

public class MinesContainer extends Container {

	@Inject
	private ObjectFactory factory;

	@Initialize
	private void initialize()
	{
		this.install(MinesService.class);
	}

}
