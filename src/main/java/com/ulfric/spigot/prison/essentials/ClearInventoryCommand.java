package com.ulfric.spigot.prison.essentials;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Alias;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.command.argument.Argument;
import com.ulfric.commons.spigot.text.Text;
import com.ulfric.spigot.prison.metadata.PrisonMetadataDefaults;

@Name("clearinventory")
@Alias({"clearinv", "ci"})
@Permission("clearinventory-use")
public class ClearInventoryCommand implements Command {

	@Argument(optional = true)
	@Permission("clearinventory-others")
	private Player target;

	@Override
	public void run(Context context)
	{
		CommandSender sender = context.getSender();
		Player target = this.target;
		if (target == null)
		{
			if (sender instanceof Player)
			{
				target = (Player) sender;
			}
			else
			{
				// TODO error
				return;
			}
		}

		target.getInventory().clear();
		target.updateInventory();

		Text text = Text.getService();
		if (target == sender)
		{
			text.sendMessage(target, "clearinventory-self");
			return;
		}

		text.sendMessage(sender, "clearinventory-other-sender",
				PrisonMetadataDefaults.LAST_CLEARED_INVENTORY_TARGET, target.getName());
	}

}