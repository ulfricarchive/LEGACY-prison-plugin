package com.ulfric.spigot.prison.essentials;

import org.bukkit.WeatherType;
import org.bukkit.entity.Player;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.text.Text;

@Name("sun")
@Permission("sun-use")
@MustBePlayer
class SunCommand implements Command {

	@Override
	public void run(Context context)
	{
		Player player = (Player) context.getSender();

		player.setPlayerWeather(WeatherType.CLEAR);

		Text.getService().sendMessage(player, "sun-use");
	}

}