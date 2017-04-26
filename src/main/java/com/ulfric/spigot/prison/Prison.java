package com.ulfric.spigot.prison;

import com.ulfric.commons.spigot.plugin.UlfricPlugin;
import com.ulfric.spigot.prison.command.EssentialCommandsContainer;
import com.ulfric.spigot.prison.mines.MinesContainer;
import com.ulfric.spigot.prison.perk.PerksContainer;
import com.ulfric.spigot.prison.rankup.RankupContainer;

public final class Prison extends UlfricPlugin {

	@Override
	public void init()
	{
		this.install(PerksContainer.class);
		this.install(EssentialCommandsContainer.class);
		this.install(RankupContainer.class);
		this.install(MinesContainer.class);
	}

}
