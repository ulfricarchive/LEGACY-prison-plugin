package com.ulfric.spigot.prison.perk;

import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;

public class PerksContainer extends Container {

	@Initialize
	private void initialize()
	{
		this.install(ChatDelayContainer.class);
		this.install(JoinWhenFullContainer.class);
		this.install(ColorSignContainer.class);
		this.install(DeathExperienceContainer.class);
	}

}
