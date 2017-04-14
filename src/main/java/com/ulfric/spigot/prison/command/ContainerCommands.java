package com.ulfric.prison.command;

import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;

public class ContainerCommands extends Container {

	@Initialize
	public void setup()
	{
		this.install(CommandFeed.class);
	}

}
