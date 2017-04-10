package com.ulfric.prison;

import com.ulfric.commons.spigot.plugin.UlfricPlugin;
import com.ulfric.dragoon.ObjectFactory;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.scope.Shared;
import com.ulfric.dragoon.scope.SharedScopeStrategy;
import com.ulfric.prison.chat.FeatureChatDelay;

public final class Prison extends UlfricPlugin {

	private final Container container = this.getContainer();

	private ObjectFactory factory; // Use black magic to get factory
	private SharedScopeStrategy sharedStrategy;

	@Override
	public void init()
	{
		this.sharedStrategy = this.getSharedStrategy();

		this.shareAndInstall(FeatureChatDelay.class);
	}

	private void shareAndInstall(Class<?> feature)
	{
		this.sharedStrategy.getOrCreate(feature);
		this.container.install(feature);
	}

	private SharedScopeStrategy getSharedStrategy()
	{
		return (SharedScopeStrategy) this.factory.request(Shared.class);
	}

}
