package com.ulfric.spigot.prison.minerocket;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Alias;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.text.Text;

@Name("minerocket")
@Alias("mr")
@Permission("minerocket-use")
class MineRocketCommand implements Command {
	
	@Override
	public void run(Context context)
	{
		Text.getService().sendMessage(context.getSender(), "minerocket-help");
	}
	
}
