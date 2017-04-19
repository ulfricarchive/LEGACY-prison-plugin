package com.ulfric.spigot.prison.spawn;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.text.Text;
import org.bukkit.entity.Player;

@Name("spawn")
@Permission("spawn-use")
@MustBePlayer
public class SpawnCommand implements Command {

	@Override
	public void run(Context context)
	{
		Player player = (Player) context.getSender();
		
		Spawn spawn = Spawn.getService();

		if (spawn.isSpawnSet())
		{
			player.teleport(spawn.getSpawn());

			Text.getService().sendMessage(player, "spawn-use");
		}
		else
		{
			Text.getService().sendMessage(player, "spawn-not-set");
		}

	}

}
