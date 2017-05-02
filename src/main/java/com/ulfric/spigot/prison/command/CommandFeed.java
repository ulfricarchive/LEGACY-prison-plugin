package com.ulfric.spigot.prison.command;

import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.text.Text;
import org.bukkit.entity.Player;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.Permission;

@Name("feed")
@Permission("feed-use")
@MustBePlayer
class CommandFeed implements Command {

	private static final int MAX_FOOD_LEVEL = 25;

	@Override
	public void run(Context context)
	{
		Player player = (Player) context.getSender();

		player.setFoodLevel(CommandFeed.MAX_FOOD_LEVEL);

		Text.getService().sendMessage(player, "feed-use");
	}

}
