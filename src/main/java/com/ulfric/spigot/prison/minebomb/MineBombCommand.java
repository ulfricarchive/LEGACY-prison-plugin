package com.ulfric.spigot.prison.minebomb;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Alias;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.text.Text;

@Name("minebomb")
@Alias("mb")
@Permission("minebomb-use")
class MineBombCommand implements Command {
	
	@Override
	public void run(Context context)
	{
		Text.getService().sendMessage(context.getSender(), "minebomb-help");
	}
	
}
