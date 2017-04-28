package com.ulfric.spigot.prison.command;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.text.Text;
import com.ulfric.spigot.prison.metadata.PrisonMetadataDefaults;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Name("blocks")
@Permission("blocks-use")
@MustBePlayer
public class CommandBlocks implements Command {
	
	private static final String BLOCKS_USE_ALL_PERMISSION = "blocks-use-all";
	
	@Override
	public void run(Context context)
	{
		Player player = (Player) context.getSender();
		
		Map<Translation, Integer> collector = this.collect(player);
		
		collector.forEach((translation, integer) ->
		{
			int blocks = (int) Math.floor(integer / 9D);
			
			this.take(player, new ItemStack(translation.getFrom()), integer * 9);
			this.give(player, new ItemStack(translation.getTo()), blocks);
		});
		
		player.updateInventory();
		
		Text.getService().sendMessage(player, "blocks-use", PrisonMetadataDefaults.LAST_BLOCKS_QUANTITY, String.valueOf(collector.values().stream().mapToInt(Integer::intValue).sum()));
	}
	
	private void give(Player player, ItemStack item, int amount)
	{
		int min = Math.min(amount, 64);
		
		item.setAmount(min);
		
		player.getInventory().addItem(item);
		
		amount -= 64;
		
		if (amount > 0)
		{
			this.give(player, item, amount);
		}
	}
	
	private void take(Player player, ItemStack item, int amount)
	{
		int min = Math.min(amount, 64);
		
		item.setAmount(min);
		
		player.getInventory().removeItem(item);
		
		amount -= 64;
		
		if (amount > 0)
		{
			this.take(player, item, amount);
		}
	}
	
	private Map<Translation, Integer> collect(Player player)
	{
		Map<Translation, Integer> collector = new HashMap<>();
		
		for (ItemStack itemStack : player.getInventory().getContents())
		{
			if (!this.isInvalid(itemStack))
			{
				Translation translation = Translation.fromOrigin(itemStack.getType());
				
				if (translation == null || !this.isAllowed(player, translation))
				{
					continue;
				}
				
				collector.compute(translation, (tran, amt) -> (amt == null ? 0 : amt) + itemStack.getAmount());
			}
		}
		
		return collector;
	}
	
	private boolean isInvalid(ItemStack itemStack)
	{
		return itemStack == null || itemStack.hasItemMeta();
	}
	
	private boolean isAllowed(Player player, Translation translation)
	{
		return player.hasPermission(CommandBlocks.BLOCKS_USE_ALL_PERMISSION) || player.hasPermission(translation.getPermission());
	}
	
	private enum Translation {
		
		COAL(Material.COAL, Material.COAL_BLOCK),
		IRON(Material.IRON_INGOT, Material.IRON_BLOCK),
		GOLD(Material.GOLD_INGOT, Material.GOLD_BLOCK),
		DIAMOND(Material.DIAMOND, Material.DIAMOND_BLOCK),
		EMERALD(Material.EMERALD, Material.EMERALD_BLOCK),
		REDSTONE(Material.REDSTONE, Material.REDSTONE_BLOCK);
		
		private static final Map<Material, Translation> ORIGINS = new EnumMap<>(Material.class);
		
		static
		{
			for (Translation translation : values())
			{
				Translation.ORIGINS.put(translation.getFrom(), translation);
			}
		}
		
		private final Material from;
		private final Material to;
		
		Translation(Material from, Material to)
		{
			this.from = from;
			this.to = to;
		}
		
		public Material getFrom()
		{
			return this.from;
		}
		
		public Material getTo()
		{
			return this.to;
		}
		
		public String getPermission()
		{
			return "blocks-use-" + this.from.name().toLowerCase();
		}
		
		public static Translation fromOrigin(Material material)
		{
			Objects.requireNonNull(material);
			
			return Translation.ORIGINS.get(material);
		}
		
	}
	
}
