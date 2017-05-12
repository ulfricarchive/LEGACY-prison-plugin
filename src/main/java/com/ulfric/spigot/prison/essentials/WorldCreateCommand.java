package com.ulfric.spigot.prison.essentials;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.command.argument.Argument;
import com.ulfric.commons.spigot.text.Text;
import com.ulfric.spigot.prison.metadata.PrisonMetadataDefaults;

@Name("create")
@Permission("world-create-use")
class WorldCreateCommand extends WorldCommand {

	@Argument
	private String worldName;

	@Override
	public void run(Context context)
	{
		CommandSender sender = context.getSender();
		Text text = Text.getService();
		
		text.sendMessage(sender, "world-creating", PrisonMetadataDefaults.LAST_WORLD_CREATED, this.worldName);
		Bukkit.createWorld(WorldCreator.name(this.worldName));
		text.sendMessage(sender, "world-created", PrisonMetadataDefaults.LAST_WORLD_CREATED, this.worldName);
	}

}
