package com.ulfric.spigot.prison.command;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.text.Text;
import org.bukkit.inventory.ItemStack;

@Name("fix")
@Permission("fix-use")
@MustBePlayer
public class CommandFix implements Command {
	
	@Override
	public void run(Context context)
	{
		Text.getService().sendMessage(context.getSender(), "fix-use-help");
	}
	
	final boolean isFixable(ItemStack itemStack)
	{
		return itemStack != null && itemStack.getDurability() != 0;
	}
	
}
