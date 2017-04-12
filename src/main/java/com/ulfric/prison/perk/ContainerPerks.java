package com.ulfric.prison.perk;

import com.ulfric.dragoon.container.Container;

public class ContainerPerks extends Container {

	@Override
	public void onLoad()
	{
		this.install(ContainerChatDelay.class);
		this.install(ContainerJoinWhenFull.class);
		this.install(ContainerColorSign.class);
	}

}
