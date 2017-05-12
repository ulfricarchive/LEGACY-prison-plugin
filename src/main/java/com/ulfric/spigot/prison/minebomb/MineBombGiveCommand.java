package com.ulfric.spigot.prison.minebomb;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.command.argument.Argument;
import com.ulfric.commons.spigot.text.Text;
import com.ulfric.spigot.prison.metadata.PrisonMetadataDefaults;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Name("give")
@Permission("minebomb-give")
class MineBombGiveCommand extends MineBombCommand {
	
	@Argument
	private Player target;
	
	@Argument
	private Integer tier;
	
	@Argument(optional = true)
	private Integer amount;
	
	@Override
	public void run(Context context)
	{
		CommandSender sender = context.getSender();
		Player target = this.target;
		int amount = this.amount == null ? 1 : this.amount;
		
		MineBombs mineBombs = MineBombs.getService();
		Text text = Text.getService();
		
		if (!mineBombs.isTier(this.tier))
		{
			text.sendMessage(sender, "minebomb-invalid-tier",
					PrisonMetadataDefaults.LAST_MINE_BOMB_INVALID_TIER, this.tier.toString());
			return;
		}
		
		mineBombs.give(target, this.tier, amount);
		
		text.sendMessage(sender, "minebomb-give",
				PrisonMetadataDefaults.LAST_MINE_BOMB_GIVE_AMOUNT, String.valueOf(amount),
				PrisonMetadataDefaults.LAST_MINE_BOMB_GIVE_USER, target.getName());
		
		text.sendMessage(target, "minebomb-receive",
				PrisonMetadataDefaults.LAST_MINE_BOMB_RECEIVE_AMOUNT, String.valueOf(amount));
	}
	
}
