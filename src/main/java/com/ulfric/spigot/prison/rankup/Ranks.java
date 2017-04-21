package com.ulfric.spigot.prison.rankup;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.service.Service;
import com.ulfric.commons.spigot.service.ServiceUtils;
import com.ulfric.commons.version.Version;
import org.bukkit.entity.Player;

import java.util.List;

@Name("ranks")
@Version(1)
public interface Ranks extends Service {
	
	static Ranks getService()
	{
		return ServiceUtils.getService(Ranks.class);
	}
	
	void addRank(Rank rank);
	
	Rank getFirst();
	
	Rank getRank(String name);
	
	boolean isRank(String name);
	
	List<Rank> getRanks();
	
	Rank getRank(Player player);
	
	Rank getNextRank(Player player);
	
	boolean hasNextRank(Player player);
	
}
