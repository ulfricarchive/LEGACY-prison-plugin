package com.ulfric.prison.chat;

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
import com.ulfric.commons.locale.Message;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.inject.Inject;
import com.ulfric.dragoon.scope.Shared;

@Shared
public final class FeatureChatDelay extends Container {

	private static final long DELAY_MILLIS = 3000;

	private final Map<UUID, Long> messageStamps = new ConcurrentHashMap<>();

	@Override
	public void onLoad()
	{
		this.install(ChatListener.class);
	}

	private static final class ChatListener implements Listener
	{

		@Inject private FeatureChatDelay chatDelay;
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

			Long timestamp = this.chatDelay.messageStamps.put(player.getUniqueId(), currentTime);

			if (this.requiresDelay(timestamp, currentTime))
			{
				event.setCancelled(true);
				Bukkit.getScheduler().runTask(this.plugin, () -> player.sendMessage(this.getMessage()));
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

		private String getMessage()
		{
			return this.locale.getMessage("chat-delay").map(Message::toString).orElse("chat-delay");
		}

	}

}
