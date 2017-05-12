package com.ulfric.spigot.prison.essentials;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.command.argument.Argument;
import com.ulfric.commons.spigot.location.WorldUtils;
import com.ulfric.commons.spigot.text.Text;
import com.ulfric.spigot.prison.metadata.PrisonMetadataDefaults;

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
		Text text = Text.getService();
		
		World world = WorldUtils.getWorld(this.worldName);

		if (world == null)
		{
			text.sendMessage(player, "world-not-found", PrisonMetadataDefaults.LAST_WORLD_LOOKUP, this.worldName);
			return;
		}

		player.teleport(world.getSpawnLocation());
		text.sendMessage(player, "world-teleported", PrisonMetadataDefaults.LAST_WORLD_LOOKUP, this.worldName);
	}

}
