package com.ulfric.spigot.prison.minebomb;

import com.ulfric.commons.spigot.block.BlockUtils;
import com.ulfric.commons.spigot.intercept.RequirePermission;
import com.ulfric.commons.spigot.text.Text;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

class MineBombListener implements Listener {
	
	@EventHandler
	@RequirePermission(permission = "minebomb-item-use")
	private void on(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		Action action = event.getAction();
		
		if (action != Action.RIGHT_CLICK_BLOCK &&
				action != Action.RIGHT_CLICK_AIR)
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
		
		Block origin = this.getOrigin(event);
		
		this.performMineBomb(player, tier, origin);
	}

	private Block getOrigin(PlayerInteractEvent event)
	{
		Block block = event.getClickedBlock();
		
		if (block == null)
		{
			block = event.getPlayer().getLocation().getBlock();
		}
		
		return block;
	}
	
	private boolean isValidBlock(Block block)
	{
		return block.getType() != Material.AIR;
	}
	
	private boolean isBreakable(Player player, Block block)
	{
		return !BlockUtils.callArtificialBreak(player, block) && this.isValidBlock(block);
	}
	
	private void handleBlockBreak(Player player, Set<Block> blocks)
	{
		blocks.forEach(block ->
		{
			block.getDrops().forEach(player.getInventory()::addItem);
			block.setType(Material.AIR);
		});
	}
	
	private void performMineBomb(Player player, int tier, Block origin)
	{
		Set<Block> blocks = BlockUtils.getBlocksWithinRadius(origin, tier);
		blocks.add(origin);
		blocks.removeIf(block -> this.isBreakable(player, block));
		
		if (!this.validateMineBomb(player, origin, blocks))
		{
			return;
		}
		
		MineBombs.getService().take(player, tier, 1);
		
		this.handleBlockBreak(player, blocks);
	}
	
	private boolean validateMineBomb(Player player, Block origin, Set<Block> blocks)
	{
		if (!BlockUtils.callArtificialBreak(player, origin))
		{
			Text.getService().sendMessage(player, "minebomb-not-in-mine");
			return false;
		}
		
		if (blocks.isEmpty())
		{
			Text.getService().sendMessage(player, "minebomb-no-surrounding-blocks");
			return false;
		}
		
		return true;
	}
	
}
