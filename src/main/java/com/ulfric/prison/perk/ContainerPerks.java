package com.ulfric.prison.perk;

import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;

public class ContainerPerks extends Container {

	@Initialize
	public void setup()
	{
		this.install(ContainerChatDelay.class);
		this.install(ContainerJoinWhenFull.class);
		this.install(ContainerColorSign.class);
	}

}
