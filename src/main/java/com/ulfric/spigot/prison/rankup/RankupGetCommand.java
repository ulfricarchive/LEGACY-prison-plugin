package com.ulfric.spigot.prison.rankup;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Alias;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.command.argument.Argument;
import com.ulfric.commons.spigot.metadata.Metadata;
import com.ulfric.commons.spigot.text.Text;
import com.ulfric.spigot.prison.metadata.PrisonMetadataDefaults;
import org.bukkit.entity.Player;

@Name("get")
@Alias("show")
@Permission("rankup-show")
@MustBePlayer
public class RankupGetCommand extends RankupCommand {
	 
	@Argument(optional = true)
	private Player player;
	
	@Override
	public void run(Context context)
	{
		Player player = (Player) context.getSender();
		Ranks ranks = Ranks.getService();
		Text text = Text.getService();
		
		if (this.player == null)
		{
			Rank rank = ranks.getRank(player);
			Metadata.write(player, PrisonMetadataDefaults.LAST_RANKUP_VIEW, rank.getName());
			text.sendMessage(player, "rankup-show");
		}
		else if	(!player.hasPermission("rankup-show-others"))
		{
			text.sendMessage(player, "rankup-no-permission");
		}
		else
		{
			Rank rank = ranks.getRank(this.player);
			Metadata.write(player, PrisonMetadataDefaults.LAST_RANKUP_VIEW, rank.getName());
			Metadata.write(player, PrisonMetadataDefaults.LAST_RANKUP_VIEW_USER, this.player.getName());
			text.sendMessage(player, "rankup-show-other");
		}
		
	}
	
}
