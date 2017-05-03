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
	
	void give(Player player, String cosmetic, int amount);
	
	default void give(Player player, String name)
	{
		this.give(player, name, 1);
	}
	
	void take(Player player, String name, int amount);
	
	default void take(Player player, String name)
	{
		this.take(player, name, 1);
	}
	
}
