package com.ulfric.spigot.prison.cosmetic.cosmetics;

import com.ulfric.commons.naming.Name;
import com.ulfric.spigot.prison.cosmetic.CosmeticItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Name("minerocket")
public class MineRocketCosmetic implements CosmeticItem {
	
	@Override
	public ItemStack getCosmetic()
	{
		return null;
	}
	
	@Override
	public boolean run(Player player)
	{
		return false;
	}
	
}
