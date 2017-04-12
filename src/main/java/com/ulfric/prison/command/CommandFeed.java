package com.ulfric.prison.command;

import org.bukkit.entity.Player;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.Permission;

@Name("feed")
@Permission("feed-use")
class CommandFeed implements Command {

	@Override
	public void run(Context context)
	{
		if (!(context.getSender() instanceof Player))
		{

		}
	}

}
