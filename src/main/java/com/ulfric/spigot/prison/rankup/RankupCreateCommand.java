package com.ulfric.spigot.prison.rankup;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.command.argument.Argument;
import com.ulfric.commons.spigot.text.Text;
import org.bukkit.command.CommandSender;

@Name("create")
@Permission("rankup-create")
public class RankupCreateCommand extends RankupCommand
{
	
	@Argument
	private String name;
	
	@Argument
	private Long price;
	
	@Override
	public void run(Context context)
	{
		Ranks ranks = Ranks.getService();
		CommandSender sender = context.getSender();
		
		if (ranks.isRank(this.name))
		{
			Text.getService().sendMessage(sender, "rankup-rank-exists");
		}
		else
		{
			Rank rank = Rank.builder()
					.setName(this.name)
					.setPrice(this.price)
					.build();
			
			ranks.addRank(rank);
			
			Text.getService().sendMessage(sender, "rankup-rank-create");
		}
		
	}
	
}
