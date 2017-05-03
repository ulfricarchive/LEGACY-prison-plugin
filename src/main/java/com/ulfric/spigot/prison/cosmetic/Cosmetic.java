package com.ulfric.spigot.prison.cosmetic;

import com.ulfric.commons.naming.Named;
import org.bukkit.entity.Player;

public interface Cosmetic extends Named {
	
	boolean run(Player player, int tier);
	
}
