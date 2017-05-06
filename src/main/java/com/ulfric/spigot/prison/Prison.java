package com.ulfric.spigot.prison;

import com.ulfric.commons.spigot.plugin.UlfricPlugin;
import com.ulfric.spigot.prison.essentials.EssentialsContainer;
import com.ulfric.spigot.prison.minebomb.MineBombContainer;
import com.ulfric.spigot.prison.minerocket.MineRocketContainer;
import com.ulfric.spigot.prison.mines.MinesContainer;
import com.ulfric.spigot.prison.perk.PerksContainer;
import com.ulfric.spigot.prison.plots.PlotsContainer;
import com.ulfric.spigot.prison.rankup.RankupContainer;
import com.ulfric.spigot.prison.spawner.SpawnersContainer;

public final class Prison extends UlfricPlugin {

	@Override
	public void init()
	{
		this.install(EssentialsContainer.class);
		this.install(PerksContainer.class);
		this.install(RankupContainer.class);
		this.install(MinesContainer.class);
		this.install(SpawnersContainer.class);
		this.install(MineBombContainer.class);
		this.install(MineRocketContainer.class);
		this.install(PlotsContainer.class);
	}

}
