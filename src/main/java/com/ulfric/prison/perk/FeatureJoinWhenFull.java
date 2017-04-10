package com.ulfric.prison.perk;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.ulfric.dragoon.container.Container;

class FeatureJoinWhenFull extends Container {

	@Override
	public void onLoad()
	{
		this.install(JoinListener.class);
	}

	private static class JoinListener implements Listener
	{

		@EventHandler
		public void on(PlayerLoginEvent event)
		{
			if (this.joinedWhenFull(event) && this.canJoinWhenFull(event.getPlayer()))
			{
				this.allowToJoin(event);
			}
		}

		private boolean joinedWhenFull(PlayerLoginEvent event)
		{
			return event.getResult() == PlayerLoginEvent.Result.KICK_FULL;
		}

		private boolean canJoinWhenFull(Player player)
		{
			return player.hasPermission("maxjoin.bypass");
		}

		private void allowToJoin(PlayerLoginEvent event)
		{
			event.setResult(PlayerLoginEvent.Result.ALLOWED);
		}

	}

}
