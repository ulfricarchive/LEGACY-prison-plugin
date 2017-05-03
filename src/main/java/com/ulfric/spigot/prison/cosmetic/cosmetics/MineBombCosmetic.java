package com.ulfric.spigot.prison.cosmetic.cosmetics;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.guard.Flag;
import com.ulfric.commons.spigot.guard.Guard;
import com.ulfric.commons.spigot.item.Item;
import com.ulfric.commons.spigot.item.MaterialType;
import com.ulfric.spigot.prison.cosmetic.CosmeticItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

@Name("minebomb")
public class MineBombCosmetic implements CosmeticItem {
	
	private final Guard guard;
	
	public MineBombCosmetic()
	{
		this.guard = Guard.getService();
	}
	
	@Override
	public boolean run(Player player, int tier)
	{
		Location location = player.getLocation();
		
		if (this.isNotAllowed(location.getBlock()))
		{
			return false;
		}
		
		Set<Block> blocks = this.getBlocks(location, tier);
		
		if (blocks.isEmpty())
		{
			return false;
		}
		
		blocks.forEach(block ->
		{
			block.getDrops().forEach(item -> player.getInventory().addItem(item));
			block.setType(Material.AIR);
		});
		
		return true;
	}
	
	private Set<Block> getBlocks(Location origin, int tier)
	{
		Set<Block> blocks = new HashSet<>();
		Block block = origin.getBlock();
		blocks.add(block);
		
		for (int x = block.getX() - tier; x <= block.getX() + tier; x++)
		{
			for (int y = block.getY() - tier; y <= block.getY() + tier; y++)
			{
				for (int z = block.getZ() - tier; z <= block.getZ() + tier; z++)
				{
					Block current = origin.getWorld().getBlockAt(x, y, z);
					
					blocks.add(current);
				}
			}
		}
		
		blocks.removeIf(this::isNotAllowed);
		
		return blocks;
	}
	
	@SuppressWarnings("unchecked")
	private boolean isNotAllowed(Block block)
	{
		return !guard.isAllowed(block.getLocation(), (Flag<Boolean>) guard.getFlag("Break"));
	}
	
	@Override
	public ItemStack getCosmetic(int tier)
	{
		return Item.builder()
				.setMaterialType(MaterialType.getType(Material.FIREBALL, (byte) 0))
				.setDisplayName("&6Mine Bomb &7(Right Click)")
				.setLore(
						"&6&l* &6Level: &7" + String.valueOf(tier)
				)
				.colorize()
				.build()
				.getItemStack();
	}
	
}
