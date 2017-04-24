package com.ulfric.spigot.prison.rankup;

import com.ulfric.commons.identity.Identifiable;
import com.ulfric.commons.identity.Identity;
import com.ulfric.commons.spigot.data.Data;
import com.ulfric.commons.spigot.data.DataStore;
import com.ulfric.commons.spigot.data.PersistentData;
import com.ulfric.commons.spigot.economy.CurrencyAmount;
import com.ulfric.commons.spigot.permissions.PermissionEntity;
import com.ulfric.commons.spigot.permissions.Permissions;
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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RankService implements Ranks {
	
	@Inject
	private Container owner;
	
	private DataStore folder;
	
	private final LinkedList<Rank> orderedRanks = new LinkedList<>();
	private final Map<String, Rank> ranks = new CaseInsensitiveMap<>();
	private final Map<UUID, Rank> userRanks = new ConcurrentHashMap<>();
	
	@Initialize
	public void initialize()
	{
		this.folder = Data.getDataStore(this.owner).getDataStore("ranks");
		
		this.ranks.clear();
		this.loadRanks(this.folder.getData("ranks"));
		
		this.order();
	}
	
	private void loadRanks(PersistentData data)
	{
		data.getSections().forEach(this::loadRank);
	}
	
	private void loadRank(PersistentData data)
	{
		String name = this.loadRankName(data);
		
		Rank rank = Rank.builder()
				.setName(name)
				.setCurrencyAmount(CurrencyAmount.parseCurrencyAmount(data.getString("price")))
				.build();
		
		this.ranks.put(rank.getName(), rank);
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
		return this.userRanks.computeIfAbsent(player.getUniqueId(), uuid ->
		{
			Rank rank = getRankFromParent(uuid);
			return rank == null ? this.getFirst() : rank;
		});
	}
	
	@Override
	public Rank getNextRank(Player player)
	{
		Rank current = this.getRank(player);
		
		if (current == null)
		{
			return null;
		}
		
		int index = this.orderedRanks.indexOf(current);
		
		if (++index < this.orderedRanks.size())
		{
			return this.orderedRanks.get(index);
		}
		
		return null;
	}
	
	private Rank getRankFromParent(UUID uuid)
	{
		Permissions permissions = Permissions.getService();
		
		PermissionEntity target = permissions.getPermissionEntity(Identity.of(uuid));
		
		List<String> names = target.getParents().map(Identifiable::getIdentity).map(Identity::toString).filter(this::isRank).collect(Collectors.toList());
		
		if (names.isEmpty())
		{
			return null;
		}
		
		List<Rank> ranks = new ArrayList<>();
		
		names.forEach(name -> ranks.add(getRank(name)));
		
		ranks.sort(Rank::compareTo);
		
		return ranks.get(0);
	}
	
	@Override
	public boolean hasNextRank(Player player)
	{
		return this.getNextRank(player) != null;
	}
	
	@Override
	public void setRank(Player player, Rank rank)
	{
		Objects.requireNonNull(rank, "rank");
		
		this.removeCurrentRank(player);
		
		Permissions permissions = Permissions.getService();
		
		PermissionEntity target = permissions.getPermissionEntity(Identity.of(player.getUniqueId()));
		PermissionEntity parent = permissions.getPermissionEntity(Identity.of(rank.getName()));
		
		target.add(parent);
		permissions.writePermissionEntity(target);
		
		this.userRanks.put(player.getUniqueId(), rank);
	}
	
	private void removeCurrentRank(Player player)
	{
		Rank rank = this.userRanks.remove(player.getUniqueId());
		
		if (rank != null)
		{
			Permissions permissions = Permissions.getService();
			
			PermissionEntity target = permissions.getPermissionEntity(Identity.of(player.getUniqueId()));
			PermissionEntity parent = permissions.getPermissionEntity(Identity.of(rank.getName()));
			
			target.remove(parent);
			permissions.writePermissionEntity(target);
		}
	}
	
}
