package com.ulfric.spigot.prison.minebomb;

import com.ulfric.commons.spigot.event.Events;
import com.ulfric.commons.spigot.intercept.RequirePermission;
import com.ulfric.commons.spigot.text.Text;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

class MineBombListener implements Listener {
	
	@EventHandler
	@RequirePermission(permission = "minebomb-item-use")
	private void on(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		
		if (event.getAction() != Action.RIGHT_CLICK_AIR ||
				event.getAction() != Action.RIGHT_CLICK_BLOCK)
		{
			return;
		}
		
		ItemStack item = event.getItem();
		
		Integer tier = MineBombs.getService().getTier(item);
		if (tier == null || tier <= 0)
		{
			return;
		}
		
		event.setCancelled(true);
		
		Block block = event.getClickedBlock();
		if (block == null)
		{
			block = player.getLocation().getBlock();
		}
		
		if (!this.callFakeBreak(player, block))
		{
			Text.getService().sendMessage(player, "minebomb-not-in-mine");
			return;
		}
		
		Set<Block> blocks = this.getBlocks(player, block, tier);
		
		if (blocks.isEmpty())
		{
			Text.getService().sendMessage(player, "minebomb-no-surrounding-blocks");
			return;
		}
		
		MineBombs.getService().take(player, tier, 1);
		
		this.handleBlockBreak(player, blocks);
	}
	
	private Set<Block> getBlocks(Player player, Block origin, int radius)
	{
		Set<Block> blocks = new HashSet<>();
		blocks.add(origin);
		
		int originX = origin.getX();
		int originY = origin.getY();
		int originZ = origin.getZ();
		
		for (int x = originX - radius; x <= originX + radius; x++)
		{
			for (int y = originY - radius; y <= originY + radius; y++)
			{
				for (int z = originZ - radius; z <= originZ + radius; z++)
				{
					Block current = origin.getWorld().getBlockAt(x, y, z);
					
					if (!this.callFakeBreak(player, current) || !this.isValidBlock(current))
					{
						continue;
					}
					
					blocks.add(current);
				}
			}
		}
		
		return blocks;
	}
	
	private boolean callFakeBreak(Player player, Block block)
	{
		return Events.fire(new BlockBreakEvent(block, player)).isCancelled();
	}
	
	private boolean isValidBlock(Block block)
	{
		return block.getType() != Material.AIR;
	}
	
	private void handleBlockBreak(Player player, Set<Block> blocks)
	{
		blocks.forEach(block ->
		{
			block.getDrops().forEach(player.getInventory()::addItem);
			block.setType(Material.AIR);
		});
	}
	
}
