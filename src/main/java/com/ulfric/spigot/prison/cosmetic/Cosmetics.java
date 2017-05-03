package com.ulfric.spigot.prison.cosmetic;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.service.Service;
import com.ulfric.commons.spigot.service.ServiceUtils;
import com.ulfric.commons.version.Version;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Name("cosmetics")
@Version(1)
public interface Cosmetics extends Service {
	
	public static Cosmetics getService()
	{
		return ServiceUtils.getService(Cosmetics.class);
	}
	
	void register(Class<? extends Cosmetic> cosmetic);
	
	Cosmetic getCosmetic(String name);
	
	CosmeticItem match(ItemStack itemStack);
	
	List<Cosmetic> getAllCosmetics();
	
	List<CosmeticItem> getCosmeticItems();
	
	void give(Player player, Cosmetic cosmetic, int tier, int amount);
	
	default void give(Player player, String name, int tier, int amount)
	{
		Cosmetic cosmetic = this.getCosmetic(name);
		
		if (cosmetic != null)
		{
			this.give(player, cosmetic, tier, amount);
		}
	}
	
	default void give(Player player, String name)
	{
		this.give(player, name, 1, 1);
	}
	
	void take(Player player, Cosmetic cosmetic, int tier, int amount);
	
	default void take(Player player, String name, int tier, int amount)
	{
		Cosmetic cosmetic = this.getCosmetic(name);
		
		if (name != null)
		{
			this.take(player, cosmetic, tier, amount);
		}
	}
	
	default void take(Player player, String name)
	{
		this.take(player, name, 1, 1);
	}
	
}
