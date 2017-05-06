package com.ulfric.spigot.prison.minerocket;

import com.ulfric.commons.spigot.block.BlockUtils;
import com.ulfric.commons.spigot.intercept.RequirePermission;
import com.ulfric.commons.spigot.text.Text;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

class MineRocketListener implements Listener {
	
	private static final BlockFace[] FACES = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN };
	
	@EventHandler
	@RequirePermission(permission = "minerockets-use")
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
		
		Integer tier = MineRockets.getService().getTier(item);
		if (tier == null || tier <= 0)
		{
			return;
		}
		
		event.setCancelled(true);
		
		Block origin = this.getOrigin(event);
		
		this.performMineRocket(player, origin, tier);
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
	
	private void performMineRocket(Player player, Block origin, int tier)
	{
		Set<Block> blocks = BlockUtils.getBlocks(origin, tier);
		blocks.add(origin);
		blocks.removeIf(block -> !this.isBreakable(player, block));
		
		if (!this.validateMineRocket(player, origin, blocks))
		{
			return;
		}
		
		MineRockets.getService().take(player, tier, 1);
		
		this.handleBlockBreak(player, blocks);
		
		BlockFace direction = this.randomDirection();
		blocks = BlockUtils.getBlocksInDirection(origin, direction, tier * 2);
		blocks.removeIf(block -> !this.isBreakable(player, block));
		
		this.handleBlockBreak(player, blocks);
	}
	
	private BlockFace randomDirection()
	{
		return MineRocketListener.FACES[ThreadLocalRandom.current().nextInt(MineRocketListener.FACES.length) - 1];
	}
	
	private void handleBlockBreak(Player player, Set<Block> blocks)
	{
		blocks.forEach(block ->
		{
			block.getDrops().forEach(player.getInventory()::addItem);
			block.setType(Material.AIR);
		});
	}
	
	private boolean validateMineRocket(Player player, Block origin, Set<Block> blocks)
	{
		if (!BlockUtils.callArtificialBreak(player, origin))
		{
			Text.getService().sendMessage(player, "minerocket-not-in-mine");
			return false;
		}
		
		if (blocks.isEmpty())
		{
			Text.getService().sendMessage(player, "minerocket-no-surrounding-blocks");
			return false;
		}
		
		return true;
	}
	
	private boolean isBreakable(Player player, Block block)
	{
		return !BlockUtils.callArtificialBreak(player, block) && this.isValidBlock(block);
	}
	
	private boolean isValidBlock(Block block)
	{
		return block.getType() != Material.AIR;
	}
	
}
