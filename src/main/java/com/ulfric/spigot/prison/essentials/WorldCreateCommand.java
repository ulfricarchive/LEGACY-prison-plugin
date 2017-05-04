package com.ulfric.spigot.prison.essentials;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.command.argument.Argument;
import com.ulfric.commons.spigot.text.Text;

@Name("create")
@Permission("world-create-use")
class WorldCreateCommand extends WorldCommand {

	@Argument
	private String worldName;

	@Override
	public void run(Context context)
	{
		CommandSender sender = context.getSender();

		Text.getService().sendMessage(sender, "world-creating");

		Bukkit.createWorld(WorldCreator.name(this.worldName));

		Text.getService().sendMessage(sender, "world-created");
	}

}
