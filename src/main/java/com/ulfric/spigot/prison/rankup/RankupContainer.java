package com.ulfric.spigot.prison.rankup;

import com.ulfric.commons.spigot.service.ServiceUtils;
import com.ulfric.dragoon.ObjectFactory;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;

public class RankupContainer extends Container {
		
	@Inject
	private ObjectFactory factory;
	
	private RankService ranks;
	
	@Initialize
	public void setup()
	{
		this.factory.bind(Ranks.class).to(RankService.class);
		this.install(RankService.class);
		this.install(RankupCommand.class);
		this.install(RankupGetCommand.class);
		this.install(RanksCommand.class);
		this.install(RankPlaceholder.class);
	}
	
	@Override
	public void onEnable()
	{
		this.ranks = ServiceUtils.getService(RankService.class);
	}
	
	@Override
	public void onDisable()
	{
		ServiceUtils.unregister(RankService.class, this.ranks);
	}
	
}
