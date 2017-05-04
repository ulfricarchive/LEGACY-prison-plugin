package com.ulfric.spigot.prison.essentials;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.item.MaterialType;
import com.ulfric.commons.spigot.text.Text;
import com.ulfric.spigot.prison.metadata.PrisonMetadataDefaults;

@Name("blocks")
@Permission("blocks-use")
@MustBePlayer
class BlocksCommand implements Command {
	
	private static final String BLOCKS_USE_ALL_PERMISSION = "blocks-use-all";
	
	@Override
	public void run(Context context)
	{
		Player player = (Player) context.getSender();
		
		Map<Translation, Integer> collector = this.collect(player);
		
		collector.forEach((translation, integer) ->
		{
			Item from = translation.getFrom();
			
			int blocks = (int) Math.floor(integer / from.getQuantity());
			
			this.take(player, new ItemStack(from.getMaterial().getType(), 1, from.getMaterial().getData()), blocks * from.getQuantity());
			this.give(player, new ItemStack(translation.getTo().getType(), 1, translation.getTo().getData()), blocks);
		});
		
		player.updateInventory();
		
		Text.getService().sendMessage(player, "blocks-use", PrisonMetadataDefaults.LAST_BLOCKS_QUANTITY,
				String.valueOf(collector.values().stream().mapToInt(Integer::intValue).sum()));
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
		return player.hasPermission(BlocksCommand.BLOCKS_USE_ALL_PERMISSION) || player.hasPermission(translation.getPermission());
	}
	
	private enum Translation {
		
		COAL(new Item(MaterialType.getType(Material.COAL), 9), MaterialType.getType(Material.COAL_BLOCK)),
		IRON(new Item(MaterialType.getType(Material.IRON_INGOT), 9), MaterialType.getType(Material.IRON_BLOCK)),
		GOLD(new Item(MaterialType.getType(Material.GOLD_INGOT), 9), MaterialType.getType(Material.GOLD_BLOCK)),
		DIAMOND(new Item(MaterialType.getType(Material.DIAMOND), 9), MaterialType.getType(Material.DIAMOND_BLOCK)),
		EMERALD(new Item(MaterialType.getType(Material.EMERALD), 9), MaterialType.getType(Material.EMERALD_BLOCK)),
		REDSTONE(new Item(MaterialType.getType(Material.REDSTONE), 9), MaterialType.getType(Material.REDSTONE_BLOCK)),
		QUARTZ(new Item(MaterialType.getType(Material.QUARTZ), 4), MaterialType.getType(Material.QUARTZ_BLOCK)),
		WHEAT(new Item(MaterialType.getType(Material.WHEAT), 9), MaterialType.getType(Material.HAY_BLOCK)),
		LAPIS(new Item(MaterialType.getType(Material.INK_SACK, (byte) 4), 9), MaterialType.getType(Material.LAPIS_BLOCK))
		
		;
		
		private static final Map<Material, Translation> ORIGINS = new EnumMap<>(Material.class);
		
		static
		{
			for (Translation translation : values())
			{
				Translation.ORIGINS.put(translation.getFrom().getMaterial().getType(), translation);
			}
		}
		
		private final Item from;
		private final MaterialType to;
		
		Translation(Item from, MaterialType to)
		{
			this.from = from;
			this.to = to;
		}
		
		public Item getFrom()
		{
			return this.from;
		}
		
		public MaterialType getTo()
		{
			return this.to;
		}
		
		public String getPermission()
		{
			return "blocks-use-" + this.from.getMaterial().getType().name().toLowerCase();
		}
		
		public static Translation fromOrigin(Material material)
		{
			Objects.requireNonNull(material);
			
			return Translation.ORIGINS.get(material);
		}
		
	}
	
	private static final class Item {
		
		private final MaterialType materialType;
		private final int quantity;
		
		Item(MaterialType materialType, int quantity)
		{
			this.materialType = materialType;
			this.quantity = quantity;
		}
		
		public MaterialType getMaterial()
		{
			return this.materialType;
		}
		
		public int getQuantity()
		{
			return this.quantity;
		}
		
	}
	
}
