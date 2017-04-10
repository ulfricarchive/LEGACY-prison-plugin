package com.ulfric.prison.perk;

import java.util.function.BiConsumer;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.ulfric.dragoon.container.Container;

class FeatureColorSign extends Container {

	@Override
	public void onLoad()
	{
		this.install(SignListener.class);
	}

	private static class SignListener implements Listener
	{

		@EventHandler
		public void on(SignChangeEvent event)
		{
			if (this.hasColorPermission(event.getPlayer()))
			{
				this.reformatLines(event.getLines(), event::setLine);
			}
		}

		private boolean hasColorPermission(Player player)
		{
			return player.hasPermission("sign.color");
		}

		private void reformatLines(String[] lines, BiConsumer<Integer, String> setLine)
		{
			for (int line = 0; line < lines.length; line++)
			{
				String lineText = lines[line];

				setLine.accept(line, this.reformatLine(lineText));
			}
		}

		private String reformatLine(String line)
		{
			if (StringUtils.isEmpty(line))
			{
				return "";
			}
			return ChatColor.translateAlternateColorCodes('&', line);
		}

	}

}
