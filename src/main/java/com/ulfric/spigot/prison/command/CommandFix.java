package com.ulfric.spigot.prison.command;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.text.Text;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

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
		return itemStack != null && itemStack.getType() != Material.AIR && itemStack.getDurability() != 0;
	}
	
	final String format(Instant instant)
	{
		long milliseconds = instant.toEpochMilli();
		long days = TimeUnit.MILLISECONDS.toDays(milliseconds) % 31;
		long hours = TimeUnit.MILLISECONDS.toHours(milliseconds) % 24;
		
		StringBuilder builder = new StringBuilder();
		
		if (days > 0)
		{
			builder.append(String.valueOf(days)).append("d ");
		}
		
		if (hours > 0)
		{
			builder.append(String.valueOf(hours)).append("h ");
		}
		
		builder.append(String.valueOf(TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60)).append("m ");
		builder.append(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60)).append("s");
		return builder.toString();
	}
	
}
