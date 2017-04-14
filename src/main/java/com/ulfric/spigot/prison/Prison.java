package com.ulfric.prison;

import com.ulfric.commons.spigot.plugin.UlfricPlugin;
import com.ulfric.prison.command.ContainerCommands;
import com.ulfric.prison.perk.ContainerPerks;

public final class Prison extends UlfricPlugin {

	@Override
	public void init()
	{
		this.install(ContainerPerks.class);
		this.install(ContainerCommands.class);
	}

}
