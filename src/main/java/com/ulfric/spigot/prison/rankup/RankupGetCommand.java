package com.ulfric.spigot.prison.rankup;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Alias;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.command.argument.Argument;
import com.ulfric.commons.spigot.text.Text;
import org.bukkit.entity.Player;

@Name("get")
@Alias("show")
@Permission("rankup-show")
public class RankupGetCommand extends RankupCommand {
	 
	@Argument(optional = true)
	private String name;
	
	@Override
	public void run(Context context)
	{
		Player player = (Player) context.getSender();
		Text text = Text.getService();
		
		if(this.name == null)
		{
			text.sendMessage(player, "rankup-show");
		}
		else if(!player.hasPermission("rankup-show-others"))
		{
			text.sendMessage(player, "rankup-no-permission");
		}
		else
		{
			text.sendMessage(player, "rankup-show-other");
		}
		
	}
	
}
