package com.ulfric.prison;

import com.ulfric.commons.spigot.plugin.UlfricPlugin;
import com.ulfric.dragoon.container.Container;
import com.ulfric.prison.command.ContainerCommands;
import com.ulfric.prison.perk.ContainerPerks;

public final class Prison extends UlfricPlugin {

	@Override
	public void init()
	{
		Container container = this.getContainer();

		container.install(ContainerPerks.class);
		container.install(ContainerCommands.class);
	}

}
