package com.ulfric.spigot.prison.rankup;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.text.Text;
import org.bukkit.entity.Player;

@Name("rankup")
@Permission("rankup-use")
@MustBePlayer
public class RankupCommand implements Command {
	
	@Override
	public void run(Context context)
	{
		Player player = (Player) context.getSender();
		
		Ranks service = Ranks.getService();
		
		if (service.hasNextRank(player))
		{
			Rank next = service.getNextRank(player);
			
			
		}
		else
		{
			Text.getService().sendMessage(player, "rankup-max-rank");
		}
		
	}
	
}
