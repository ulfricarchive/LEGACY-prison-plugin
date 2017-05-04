package com.ulfric.spigot.prison.minebomb;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.service.Service;
import com.ulfric.commons.spigot.service.ServiceUtils;
import com.ulfric.commons.version.Version;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Name("minebombs")
@Version(1)
class MineBombs implements Service {
	
	public static MineBombs getService()
	{
		return ServiceUtils.getService(MineBombs.class);
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
	
	public ItemStack getItem(int tier)
	{
		return null;
	}
	
	public Integer getTier(ItemStack item)
	{
		return null;
	}
	
}
