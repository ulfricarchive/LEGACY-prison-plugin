package com.ulfric.spigot.prison;

import com.ulfric.commons.spigot.plugin.UlfricPlugin;
import com.ulfric.spigot.prison.command.ContainerCommands;
import com.ulfric.spigot.prison.perk.ContainerPerks;
import com.ulfric.spigot.prison.rankup.RankupContainer;

public final class Prison extends UlfricPlugin {

	@Override
	public void init()
	{
//		this.install(ContainerPerks.class);
		this.install(ContainerCommands.class);
		this.install(RankupContainer.class);
	}

}
