package com.ulfric.spigot.prison.minebomb;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.service.Service;
import com.ulfric.commons.spigot.data.Data;
import com.ulfric.commons.spigot.data.DataSection;
import com.ulfric.commons.spigot.data.PersistentData;
import com.ulfric.commons.spigot.item.parts.ItemParts;
import com.ulfric.commons.spigot.service.ServiceUtils;
import com.ulfric.commons.version.Version;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;

@Name("minebombs")
@Version(1)
class MineBombs implements Service {
	
	public static MineBombs getService()
	{
		return ServiceUtils.getService(MineBombs.class);
	}
	
	@Inject
	private Container owner;
	
	private final Map<Integer, ItemStack> mineBombs = new HashMap<>();
	
	@Initialize
	private void initialize()
	{
		PersistentData data = Data.getDataStore(this.owner).getData("mb-items");
		data.getSections().forEach(this::loadMineBomb);
	}
	
	private void loadMineBomb(DataSection data)
	{
		int tier = data.getInt("tier");
		ItemStack itemStack = ItemParts.deserialize(data.getString("item"));
		
		if (itemStack != null)
		{
			this.mineBombs.putIfAbsent(tier, itemStack);
		}
	}
	
	public void give(Player player, int tier, int amount)
	{
		int min = Math.min(amount, 64);
		
		ItemStack clone = this.getItem(tier).clone();
		clone.setAmount(min);
		
		player.getInventory().addItem(clone);
		
		amount -= 64;
		
		if (amount > 0)
		{
			this.give(player, tier, amount);
		}
	}
	
	public void take(Player player, int tier, int amount)
	{
		int min = Math.min(amount, 64);
		
		ItemStack clone = this.getItem(tier).clone();
		clone.setAmount(min);
		
		amount -= 64;
		
		if (amount > 0)
		{
			this.take(player, tier, amount);
		}
	}
	
	public boolean isTier(int tier)
	{
		return this.mineBombs.containsKey(tier);
	}
	
	public ItemStack getItem(int tier)
	{
		return this.mineBombs.get(tier);
	}
	
	public Integer getTier(ItemStack item)
	{
		for (Map.Entry<Integer, ItemStack> entry : this.mineBombs.entrySet())
		{
			if (entry.getValue().isSimilar(item))
			{
				return entry.getKey();
			}
		}
		
		return null;
	}
	
}
