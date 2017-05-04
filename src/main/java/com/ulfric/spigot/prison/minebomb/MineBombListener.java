package com.ulfric.spigot.prison.minebomb;

import com.ulfric.commons.spigot.guard.Flag;
import com.ulfric.commons.spigot.guard.Guard;
import com.ulfric.commons.spigot.text.Text;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

class MineBombListener implements Listener {
	
	private final Guard guard;
	
	MineBombListener()
	{
		this.guard = Guard.getService();
	}
	
	@EventHandler
	private void on(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		
		if (event.getAction() == Action.RIGHT_CLICK_AIR ||
				event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			ItemStack item = event.getItem();
			
			Integer tier =  MineBombs.getService().getTier(item);
			
			if (tier != null && tier > 0)
			{
				event.setCancelled(true);
				
				Block block = event.getClickedBlock();
				if (block == null)
				{
					block = player.getLocation().getBlock();
				}
				
				Text text = Text.getService();
				
				if (this.isNotAllowed(block))
				{
					text.sendMessage(player, "minebombs-not-in-mine");
					return;
				}
				
				Set<Block> blocks = this.getBlocks(block, tier);
				
				if (blocks.isEmpty())
				{
					text.sendMessage(player, "minebombs-no-surrounding-blocks");
					return;
				}
				
				MineBombs.getService().take(player, tier, 1);
				
				blocks.forEach(b ->
				{
					b.getDrops().forEach(itemStack -> player.getInventory().addItem(itemStack));
					b.setType(Material.AIR);
				});
			}
		}
	}
	
	private Set<Block> getBlocks(Block origin, int radius)
	{
		Set<Block> blocks = new HashSet<>();
		blocks.add(origin);
		
		for (int x = origin.getX() - radius; x <= origin.getX() + radius; x++)
		{
			for (int y = origin.getY() - radius; y <= origin.getY() + radius; y++)
			{
				for (int z = origin.getZ() - radius; z <= origin.getZ() + radius; z++)
				{
					Block current = origin.getWorld().getBlockAt(x, y, z);
					
					blocks.add(current);
				}
			}
		}
		
		return blocks;
	}
	
	@SuppressWarnings("unchecked")
	private boolean isNotAllowed(Block block)
	{
		return !this.guard.isAllowed(block.getLocation(), (Flag<Boolean>) this.guard.getFlag("Break"));
	}
	
}
