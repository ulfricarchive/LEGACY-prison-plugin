package com.ulfric.spigot.prison.rankup;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.text.Text;

@Name("ranks")
@Permission("ranks-use")
public class RanksCommand implements Command {
	
	@Override
	public void run(Context context)
	{
		Text.getService().sendMessage(context.getSender(), "rankup-ranks");
	}
	
}
