package com.ulfric.spigot.prison.minerocket;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.command.argument.Argument;
import com.ulfric.commons.spigot.text.Text;
import com.ulfric.spigot.prison.metadata.PrisonMetadataDefaults;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Name("give")
@Permission("minerocket-give")
class MineRocketGiveCommand extends MineRocketCommand {
	
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
		
		MineRockets mineRockets = MineRockets.getService();
		Text text = Text.getService();
		
		if (!mineRockets.isTier(this.tier))
		{
			text.sendMessage(sender, "minerocket-invalid-tier");
			return;
		}
		
		mineRockets.give(target, this.tier, amount);
		
		text.sendMessage(context.getSender(), "minerocket-give",
				PrisonMetadataDefaults.LAST_MINE_ROCKET_GIVE_AMOUNT, String.valueOf(amount),
				PrisonMetadataDefaults.LAST_MINE_ROCKET_GIVE_USER, target.getName());
		
		text.sendMessage(target, "minerocket-receive",
				PrisonMetadataDefaults.LAST_MINE_ROCKET_RECEIVE_AMOUNT, String.valueOf(amount));
	}
	
}
