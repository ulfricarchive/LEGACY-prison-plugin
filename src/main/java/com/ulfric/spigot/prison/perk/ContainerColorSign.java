package com.ulfric.spigot.prison.perk;

import java.util.function.BiConsumer;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.ulfric.commons.spigot.intercept.RequirePermission;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;

class ContainerColorSign extends Container {

	@Initialize
	public void setup()
	{
		this.install(SignListener.class);
	}

	private static class SignListener implements Listener
	{

		@EventHandler
		@RequirePermission(permission = "sign-color")
		public void on(SignChangeEvent event)
		{
			this.reformatLines(event.getLines(), event::setLine);
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
