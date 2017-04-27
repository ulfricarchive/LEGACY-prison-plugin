package com.ulfric.spigot.prison.mines;

import com.ulfric.commons.spigot.service.ServiceUtils;
import com.ulfric.dragoon.ObjectFactory;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;

public class MinesContainer extends Container {

	@Inject
	private ObjectFactory factory;
	
	private MinesService mines;
	
	@Initialize
	private void initialize()
	{
		this.factory.bind(Mines.class).to(MinesService.class);
		this.install(MinesService.class);
	}
	
	@Override
	public void onEnable()
	{
		this.mines = ServiceUtils.getService(MinesService.class);
	}
	
	@Override
	public void onDisable()
	{
		ServiceUtils.unregister(MinesService.class, this.mines);
	}
	
}
