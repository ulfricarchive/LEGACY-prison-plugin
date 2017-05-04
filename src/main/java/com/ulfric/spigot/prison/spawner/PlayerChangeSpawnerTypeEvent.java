package com.ulfric.spigot.prison.spawner;

import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerChangeSpawnerTypeEvent extends PlayerEvent implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();

	public static HandlerList getHandlerList()
	{
		return PlayerChangeSpawnerTypeEvent.HANDLERS;
	}

	private final CreatureSpawner spawner;
	private final EntityType newType;
	private boolean cancel;

	PlayerChangeSpawnerTypeEvent(Player who, CreatureSpawner spawner, EntityType newType)
	{
		super(who);

		this.spawner = spawner;
		this.newType = newType;
	}

	public CreatureSpawner getSpawner()
	{
		return this.spawner;
	}

	public EntityType getNewType()
	{
		return this.newType;
	}

	@Override
	public boolean isCancelled()
	{
		return this.cancel;
	}

	@Override
	public void setCancelled(boolean cancel)
	{
		this.cancel = cancel;
	}

	@Override
	public HandlerList getHandlers()
	{
		return PlayerChangeSpawnerTypeEvent.HANDLERS;
	}

}