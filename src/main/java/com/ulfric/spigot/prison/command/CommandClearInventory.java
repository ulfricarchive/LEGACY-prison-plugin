package com.ulfric.spigot.prison.command;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Alias;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.command.argument.Argument;
import com.ulfric.commons.spigot.text.Text;
import com.ulfric.spigot.prison.metadata.PrisonMetadataDefaults;
import org.bukkit.entity.Player;

@Name("clearinventory")
@Alias("ci")
@Permission("clearinventory-use")
@MustBePlayer
public class CommandClearInventory implements Command {
	
	@Argument(optional = true)
	private Player target;
	
	@Override
	public void run(Context context)
	{
		Player player = (Player) context.getSender();
		
		Text text = Text.getService();
		
		if (this.target == null)
		{
			player.getInventory().clear();
			player.updateInventory();
			text.sendMessage(player, "clearinventory-use");
			return;
		}
		
		if (!player.hasPermission("clearinventory-others"))
		{
			text.sendMessage(player, "clearinventory-others-no-permission");
			return;
		}
		
		Player target = this.target;
		
		target.getInventory().clear();
		target.updateInventory();
		
		text.sendMessage(target, "clearinventory-other-target", PrisonMetadataDefaults.LAST_CLEARED_INVENTORY_SENDER, player.getName());
		text.sendMessage(player, "clearinventory-other-sender", PrisonMetadataDefaults.LAST_CLEARED_INVENTORY_TARGET, target.getName());
	}
	
}
