package com.ulfric.spigot.prison.cosmetic;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface CosmeticItem extends Cosmetic {
	
	ItemStack getCosmetic(int tier);
	
	default boolean hasCosmetic(Inventory inventory, int tier)
	{
		ItemStack cosmetic = this.getCosmetic(tier);
		
		for (ItemStack itemStack : inventory.getContents())
		{
			if (itemStack == null)
			{
				continue;
			}
			
			if (cosmetic.isSimilar(itemStack))
			{
				return true;
			}
		}
		
		return false;
	}
	
	default boolean hasCosmetic(Player player, int tier)
	{
		return this.hasCosmetic(player.getInventory(), tier);
	}
	
}
