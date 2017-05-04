package com.ulfric.spigot.prison.essentials;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.cooldown.Cooldown;
import com.ulfric.commons.spigot.cooldown.CooldownAccount;
import com.ulfric.commons.spigot.cooldown.Cooldowns;
import com.ulfric.commons.spigot.text.Text;
import com.ulfric.spigot.prison.metadata.PrisonMetadataDefaults;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;

@Name("all")
@Permission("fix-all-use")
@MustBePlayer
public class FixAllCommand extends FixCommand {

	private static final String COOLDOWN_NAME = "COMMAND_FIX_ALL";

	@Override
	public void run(Context context)
	{
		Player player = (Player) context.getSender();

		CooldownAccount account = Cooldowns.getService().getAccount(player.getUniqueId());

		if (account.isCooldown(FixAllCommand.COOLDOWN_NAME))
		{
			Cooldown cooldown = account.getCooldown(FixAllCommand.COOLDOWN_NAME);
			Instant remaining = cooldown.getRemaining();
			Text.getService().sendMessage(player, "fix-all-cooldown", PrisonMetadataDefaults.LAST_FIX_ALL_COOLDOWN, this.format(remaining));
			return;
		}

		boolean repaired = this.repair(player.getInventory());

		if (repaired)
		{
			player.updateInventory();
			
			Cooldown cooldown = Cooldown.builder()
					.setUniqueId(player.getUniqueId())
					.setName(FixAllCommand.COOLDOWN_NAME)
					.setStart(Instant.now())
					.setExpiry(this.getExpiry())
					.build();
			
			account.setCooldown(cooldown);
			
			Text.getService().sendMessage(player, "fix-all");
		}

		else
		{
			Text.getService().sendMessage(player, "fix-all-invalid-items");
		}
	}

	private boolean repair(Inventory inventory)
	{
		boolean repaired = false;
		
		for (ItemStack itemStack : inventory.getContents())
		{
			if (this.isFixable(itemStack))
			{
				itemStack.setDurability((short) 0);
				repaired = true;
			}
		}
		
		return repaired;
	}

	private Instant getExpiry()
	{
		// todo: need to talk to packet
		return Instant.now();
	}

}