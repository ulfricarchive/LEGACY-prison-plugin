package com.ulfric.prison.perk;

import com.ulfric.dragoon.container.Container;

public class FeaturePerks extends Container {

	@Override
	public void onLoad()
	{
		this.install(FeatureChatDelay.class);
		this.install(FeatureJoinWhenFull.class);
		this.install(FeatureColorSign.class);
	}

}
