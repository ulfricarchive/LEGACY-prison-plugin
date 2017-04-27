package com.ulfric.spigot.prison.spawner;

import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;

public class SpawnersContainer extends Container {

	@Initialize
	private void initialize()
	{
		this.install(LastSpawnerTypePlaceholder.class);
		this.install(SpawnerCommand.class);
	}

}