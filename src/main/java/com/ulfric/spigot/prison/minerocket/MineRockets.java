package com.ulfric.spigot.prison.minerocket;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.service.Service;
import com.ulfric.commons.spigot.data.Data;
import com.ulfric.commons.spigot.data.PersistentData;
import com.ulfric.commons.spigot.item.parts.ItemParts;
import com.ulfric.commons.spigot.service.ServiceUtils;
import com.ulfric.commons.version.Version;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Name("minerockets")
@Version(1)
class MineRockets implements Service {
	
	public static MineRockets getService()
	{
		return ServiceUtils.getService(MineRockets.class);
	}
	
	private final Map<Integer, ItemStack> mineRockets = new HashMap<>();
	
	@Inject
	private Container owner;
	
	@Initialize
	private void initialize()
	{
		PersistentData data = Data.getDataStore(this.owner).getData("mr-items");
		data.getSections().forEach(this::loadMineRocket);
	}
	
	private void loadMineRocket(PersistentData data)
	{
		int tier = data.getInt("tier");
		ItemStack itemStack = ItemParts.deserialize(data.getString("item"));
		
		if (itemStack != null)
		{
			this.mineRockets.putIfAbsent(tier, itemStack);
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
		return this.mineRockets.containsKey(tier);
	}
	
	public ItemStack getItem(int tier)
	{
		return this.mineRockets.get(tier);
	}
	
	public Integer getTier(ItemStack item)
	{
		for (Map.Entry<Integer, ItemStack> entry : this.mineRockets.entrySet())
		{
			if (item.isSimilar(entry.getValue()))
			{
				return entry.getKey();
			}
		}
		
		return null;
	}
	
}
