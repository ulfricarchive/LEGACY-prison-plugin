package com.ulfric.spigot.prison.cosmetic;

import com.ulfric.commons.exception.Try;
import com.ulfric.commons.naming.Named;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;
import com.ulfric.spigot.prison.cosmetic.cosmetics.MineRocketCosmetic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.CaseInsensitiveMap;

public class CosmeticService implements Cosmetics {

	@Inject
	private Container owner;
	
	private final Map<String, Cosmetic> cosmetics = new CaseInsensitiveMap<>();
	private final Map<String, CosmeticItem> cosmeticItems = new CaseInsensitiveMap<>();
	
	@Initialize
	private void setup()
	{
		this.register(MineRocketCosmetic.class);
	}
	
	@Override
	public void register(Class<? extends Cosmetic> cosmeticClass)
	{
		Try.to(() ->
		{
			Cosmetic cosmetic = cosmeticClass.newInstance();
			
			String name = Named.getNameFromAnnotation(cosmeticClass);
			
			this.cosmetics.put(name, cosmetic);
			
			if (cosmetic instanceof CosmeticItem)
			{
				this.cosmeticItems.put(name, (CosmeticItem) cosmetic);
				
				this.owner.install(CosmeticCommand.createCosmeticCommand(cosmetic));
			}
		});
	}
	
	@Override
	public Cosmetic getCosmetic(String name)
	{
		return this.cosmetics.get(name);
	}
	
	@Override
	public CosmeticItem match(ItemStack itemStack)
	{
		return this.getCosmeticItems().stream().filter(cosmetic -> cosmetic.getCosmetic().isSimilar(itemStack)).findFirst().orElse(null);
	}
	
	@Override
	public List<Cosmetic> getAllCosmetics()
	{
		return Collections.unmodifiableList(new ArrayList<>(this.cosmetics.values()));
	}
	
	@Override
	public List<CosmeticItem> getCosmeticItems()
	{
		return Collections.unmodifiableList(new ArrayList<>(this.cosmeticItems.values()));
	}
	
	@Override
	public void give(Player player, String name, int amount)
	{
		Cosmetic cosmetic = this.getCosmetic(name);
		
		if (cosmetic == null)
		{
			return;
		}
				
		if (cosmetic instanceof CosmeticItem)
		{
			ItemStack itemStack = ((CosmeticItem) cosmetic).getCosmetic().clone();
			
			this.give(player, itemStack, amount);
		}
		else
		{
			throw new UnsupportedOperationException();
		}
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
	
	@Override
	public void take(Player player, String name, int amount)
	{
		Cosmetic cosmetic = this.getCosmetic(name);
		
		if (cosmetic == null)
		{
			return;
		}
		
		if (cosmetic instanceof CosmeticItem)
		{
			ItemStack itemStack = ((CosmeticItem) cosmetic).getCosmetic().clone();
			
			this.take(player, itemStack, amount);
		}
		else
		{
			throw new UnsupportedOperationException();
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
	
}
