package com.ulfric.prison;

import com.ulfric.commons.spigot.plugin.UlfricPlugin;
import com.ulfric.dragoon.container.Container;
import com.ulfric.prison.capacity.FeatureJoinWhenFull;
import com.ulfric.prison.chat.FeatureChatDelay;

public final class Prison extends UlfricPlugin {

	@Override
	public void init()
	{
		Container container = this.getContainer();

		container.install(FeatureChatDelay.class);
		container.install(FeatureJoinWhenFull.class);
	}

}
