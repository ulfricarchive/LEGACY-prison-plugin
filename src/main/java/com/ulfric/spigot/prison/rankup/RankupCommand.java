package com.ulfric.spigot.prison.rankup;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.economy.BalanceDeductionResult;
import com.ulfric.commons.spigot.economy.BankAccount;
import com.ulfric.commons.spigot.economy.Economy;
import com.ulfric.commons.spigot.text.Text;
import org.bukkit.entity.Player;

@Name("rankup")
@Permission("rankup-use")
@MustBePlayer
public class RankupCommand implements Command {
	
	@Override
	public void run(Context context)
	{
		Player player = (Player) context.getSender();
		
		Ranks service = Ranks.getService();
		
		if (service.hasNextRank(player))
		{
			Rank next = service.getNextRank(player);
			
			BankAccount account = Economy.getService().getAccount(player.getUniqueId());
			BalanceDeductionResult result = account.deduct(next.getCurrencyAmount());
			
			if (result.isSuccess())
			{
				service.setRank(player, next);
				
				Text.getService().sendMessage(player, "rankup");
			}
			else
			{
				Text.getService().sendMessage(player, "rankup-cannot-afford");
			}
			
		}
		else
		{
			Text.getService().sendMessage(player, "rankup-max-rank");
		}
		
	}
	
}
