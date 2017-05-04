package com.ulfric.spigot.prison.essentials;

import org.bukkit.entity.Player;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Alias;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;

@Name("enderchest")
@Alias("ec")
@Permission("enderchest-use")
@MustBePlayer
class EnderchestCommand implements Command {

	@Override
	public void run(Context context)
	{
		Player player = (Player) context.getSender();

		player.openInventory(player.getEnderChest());
	}

}