package com.ulfric.spigot.prison.rankup;

import com.ulfric.commons.spigot.data.Data;
import com.ulfric.commons.spigot.data.DataStore;
import com.ulfric.commons.spigot.data.PersistentData;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;
import org.bukkit.entity.Player;

import org.apache.commons.collections4.map.CaseInsensitiveMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RankService implements Ranks {
	
	@Inject
	private Container owner;
	
	private DataStore folder;
	
	private final LinkedList<Rank> orderedRanks = new LinkedList<>();
	private final Map<String, Rank> ranks = new CaseInsensitiveMap<>();
	
	@Initialize
	public void initialize()
	{
		this.folder = Data.getDataStore(this.owner).getDataStore("ranks");
		
		this.ranks.clear();
		this.loadRanks(this.folder.getData("ranks"));
		
		this.order();
	}
	
	private void loadRank(PersistentData data)
	{
		String name = this.loadRankName(data);
		
		Rank rank = Rank.builder()
				.setName(name)
				.setPrice(data.getInt("price"))
				.build();
		
		this.ranks.put(rank.getName(), rank);
	}
	
	private void loadRanks(PersistentData data)
	{
		data.getSections().forEach(this::loadRank);
	}
	
	private String loadRankName(PersistentData data)
	{
		String name = data.getString("name");
		return name == null ? data.getName() : name;
	}
	
	private void order()
	{
		this.orderedRanks.clear();
		this.orderedRanks.addAll(getRanks());
		this.orderedRanks.sort(Rank::compareTo);
	}
	
	@Override
	public void addRank(Rank rank)
	{
		Objects.requireNonNull(rank, "rank");
		
		String name = rank.getName();
		this.ranks.remove(name);
		this.ranks.put(name, rank);
		
		PersistentData data = this.folder.getData("ranks");
		data.set(name + ".name", name);
		data.set(name + ".price", rank.getPrice());
		data.save();
		
		this.order();
	}
	
	@Override
	public Rank getFirst()
	{
		if (this.orderedRanks.isEmpty())
		{
			return null;
		}
		
		return this.orderedRanks.getFirst();
	}
	
	@Override
	public Rank getRank(String name)
	{
		return this.ranks.get(name);
	}
	
	@Override
	public boolean isRank(String name)
	{
		return this.ranks.containsKey(name);
	}
	
	@Override
	public List<Rank> getRanks()
	{
		return Collections.unmodifiableList(new ArrayList<>(this.ranks.values()));
	}
	
	@Override
	public Rank getRank(Player player)
	{
		return null;
	}
	
	@Override
	public Rank getNextRank(Player player)
	{
		return null;
	}
	
	@Override
	public boolean hasNextRank(Player player)
	{
		return this.getNextRank(player) != null;
	}
	
}
