package com.ulfric.spigot.prison.spawn;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.text.Text;
import org.bukkit.entity.Player;

@Name("setspawn")
@Permission("setspawn-use")
@MustBePlayer
public class SetSpawnCommand implements Command {

	@Override
	public void run(Context context)
	{
		Player player = (Player) context.getSender();

		Spawn.getService().setSpawn(player.getLocation());

		Text.getService().sendMessage(player, "set-spawn");
	}

}
