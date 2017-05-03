package com.ulfric.spigot.prison.cosmetic.cosmetics;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.item.Item;
import com.ulfric.commons.spigot.item.MaterialType;
import com.ulfric.spigot.prison.cosmetic.CosmeticItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Name("minerocket")
public class MineRocketCosmetic implements CosmeticItem {
	
	@Override
	public boolean run(Player player, int tier)
	{
		return false;
	}
	
	@Override
	public ItemStack getCosmetic(int tier)
	{
		return Item.builder()
				.setMaterialType(MaterialType.getType(Material.FIREWORK, (byte) 0))
				.setDisplayName("&cMine Rocket &7(Right Click)")
				.colorize()
				.build()
				.getItemStack();
	}
	
}
