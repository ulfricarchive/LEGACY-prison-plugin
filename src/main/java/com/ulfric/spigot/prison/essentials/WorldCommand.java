package com.ulfric.spigot.prison.essentials;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.command.argument.Argument;
import com.ulfric.commons.spigot.text.Text;

@Name("world")
@Permission("world-use")
@MustBePlayer
class WorldCommand implements Command {

	@Argument
	private String worldName;

	@Override
	public void run(Context context)
	{
		Player player = (Player) context.getSender();

		World world = Bukkit.getWorld(this.worldName);

		if (world == null)
		{
			Text.getService().sendMessage(player, "world-not-found");

			return;
		}

		player.teleport(world.getSpawnLocation());

		Text.getService().sendMessage(player, "world-teleported");
	}

}
