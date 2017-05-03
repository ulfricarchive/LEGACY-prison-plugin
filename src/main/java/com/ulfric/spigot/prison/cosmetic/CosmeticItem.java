package com.ulfric.spigot.prison.cosmetic;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface CosmeticItem extends Cosmetic {
	
	ItemStack getCosmetic();
	
	default boolean hasCosmetic(Inventory inventory)
	{
		ItemStack cosmetic = this.getCosmetic();
		
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
	
	default boolean hasCosmetic(Player player)
	{
		return this.hasCosmetic(player.getInventory());
	}
	
}
