package com.ulfric.spigot.prison.command;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.text.Text;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Name("hand")
@Permission("fix-use-hand")
@MustBePlayer
public class CommandFixHand extends CommandFix {
	
	private static final long COOLDOWN_IN_MILLISECONDS = 300000;
	
	@Override
	public void run(Context context)
	{
		Player player = (Player) context.getSender();
		
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		
		if (!this.isFixable(itemStack))
		{
			Text.getService().sendMessage(player, "fix-use-invalid-item");
			return;
		}
		
		// todo: implement cooldown
		
		itemStack.setDurability((short) 0);
		
		Text.getService().sendMessage(player, "fix-use-hand");
	}
	
}
