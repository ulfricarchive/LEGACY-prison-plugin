package com.ulfric.prison.perk;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import com.ulfric.commons.locale.Locale;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.inject.Inject;

class FeatureChatDelay extends Container {

	private static final long DELAY_MILLIS = 3000;

	@Override
	public void onLoad()
	{
		this.install(ChatListener.class);
	}

	private static class ChatListener implements Listener
	{

		private final Map<UUID, Long> messageStamps = new ConcurrentHashMap<>();

		@Inject private Plugin plugin;
		@Inject private Locale locale;

		@EventHandler
		public void on(AsyncPlayerChatEvent event)
		{
			Player player = event.getPlayer();

			if (this.canBypass(player))
			{
				return;
			}

			long currentTime = System.currentTimeMillis();

			Long timestamp = this.messageStamps.put(player.getUniqueId(), currentTime);

			if (this.requiresDelay(timestamp, currentTime))
			{
				event.setCancelled(true);
				this.sendChatDelayMessage(player);
			}
		}

		private boolean canBypass(Player player)
		{
			return player.hasPermission("chatdelay.bypass");
		}

		private boolean requiresDelay(Long timestamp, long currentTime)
		{
			return timestamp != null && (currentTime - timestamp) < FeatureChatDelay.DELAY_MILLIS;
		}

		private void sendChatDelayMessage(Player player)
		{
			Bukkit.getScheduler().runTask(this.plugin, () -> player.sendMessage(this.getMessage()));
		}

		private String getMessage()
		{
			return this.locale.getMessage("chat-delay").getText();
		}

	}

}
