package com.ulfric.spigot.prison.perk;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.ulfric.commons.spigot.data.Data;
import com.ulfric.commons.spigot.data.DataStore;
import com.ulfric.commons.spigot.task.Tasks;
import com.ulfric.commons.spigot.text.Text;
import com.ulfric.dragoon.inject.Inject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;

class ChatDelayContainer extends Container {

	@Initialize
	private void initialize()
	{
		this.install(ChatListener.class);
	}

	private static class ChatListener implements Listener
	{

		@Inject
		private Container owner;

		private long delay = 3000;
		private final Map<UUID, Instant> messageStamps = new ConcurrentHashMap<>();

		@Initialize
		private void initialize()
		{
			DataStore dataStore = Data.getDataStore(this.owner).getDataStore("chat-delay");

			this.delay = dataStore.getDefault().getInt("chat-delay");
		}

		@EventHandler(ignoreCancelled = true)
		// todo: permission annotation so it won't run for people with permission 'chatdelay-bypass'
		public void on(AsyncPlayerChatEvent event)
		{
			Player player = event.getPlayer();

			Instant current = Instant.now();

			if (this.requiresDelay(player, current))
			{
				event.setCancelled(true);

				Tasks.runSync(() -> Text.getService().sendMessage(player, "chat-delay"));
			}
			else
			{
				this.messageStamps.put(player.getUniqueId(), current);
			}

		}

		private boolean requiresDelay(Player player, Instant current)
		{
			Instant instant = this.messageStamps.get(player.getUniqueId());

			return instant != null && ChronoUnit.MILLIS.between(instant, current) > this.delay;
		}

	}

}
