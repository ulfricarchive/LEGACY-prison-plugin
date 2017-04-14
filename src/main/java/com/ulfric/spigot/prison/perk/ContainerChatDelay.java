package com.ulfric.spigot.prison.perk;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.ulfric.commons.spigot.text.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import com.ulfric.commons.locale.Locale;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;

class ContainerChatDelay extends Container {

	private static final long DELAY_MILLIS = 3000;

	@Initialize
	public void setup()
	{
		this.install(ChatListener.class);
	}

	private static class ChatListener implements Listener
	{

		private final Map<UUID, Long> messageStamps = new ConcurrentHashMap<>();

		@Inject private Plugin plugin;

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
			return player.hasPermission("chatdelay-bypass");
		}

		private boolean requiresDelay(Long timestamp, long currentTime)
		{
			return timestamp != null && (currentTime - timestamp) < ContainerChatDelay.DELAY_MILLIS;
		}

		private void sendChatDelayMessage(Player player)
		{
			Bukkit.getScheduler().runTask(this.plugin, () -> sendMessage(player));
		}

		private void sendMessage(Player player)
		{
			Text.getService().sendMessage(player, "chat-delay");
		}

	}

}
