package com.ulfric.prison.perk;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.ulfric.commons.spigot.intercept.RequirePermission;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;

class ContainerJoinWhenFull extends Container {

	@Initialize
	public void setup()
	{
		this.install(JoinListener.class);
	}

	private static class JoinListener implements Listener
	{

		@EventHandler
		@RequirePermission(permission = "maxjoin-bypass")
		public void on(PlayerLoginEvent event)
		{
			if (this.joinedWhenFull(event))
			{
				this.allowToJoin(event);
			}
		}

		private boolean joinedWhenFull(PlayerLoginEvent event)
		{
			return event.getResult() == PlayerLoginEvent.Result.KICK_FULL;
		}

		private void allowToJoin(PlayerLoginEvent event)
		{
			event.setResult(PlayerLoginEvent.Result.ALLOWED);
		}

	}

}
