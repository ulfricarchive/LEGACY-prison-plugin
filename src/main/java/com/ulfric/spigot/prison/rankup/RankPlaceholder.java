package com.ulfric.spigot.prison.rankup;

import org.bukkit.entity.Player;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.text.placeholder.PlayerPlaceholder;

@Name("PLAYER_PRISON_RANK")
class RankPlaceholder implements PlayerPlaceholder {
	
	@Override
	public String apply(Player player)
	{
		Ranks ranks = Ranks.getService();
		
		Rank rank = ranks.getRank(player);
		
		return rank == null ? ranks.getFirst().getName() : rank.getName();
	}
	
}
