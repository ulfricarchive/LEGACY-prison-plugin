package com.ulfric.spigot.prison.essentials.repair;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Alias;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.text.Text;

@Name("repair")
@Alias("fix")
@Permission("repair-use")
@MustBePlayer
class RepairCommand implements Command {
	
	@Override
	public void run(Context context)
	{
		Text.getService().sendMessage(context.getSender(), "repair-help");
	}
	
	protected final boolean isFixable(ItemStack itemStack)
	{
		return itemStack != null && itemStack.getType() != Material.AIR && itemStack.getDurability() != 0;
	}
	
}
