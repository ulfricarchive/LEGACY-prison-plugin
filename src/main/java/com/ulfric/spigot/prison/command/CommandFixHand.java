package com.ulfric.spigot.prison.command;

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
import org.bukkit.inventory.ItemStack;

import java.time.Instant;

@Name("hand")
@Permission("fix-hand-use")
@MustBePlayer
public class CommandFixHand extends CommandFix {
	
	private static final String COOLDOWN_NAME = "COMMAND_FIX_HAND";
	
	@Override
	public void run(Context context)
	{
		Player player = (Player) context.getSender();
		
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		
		if (!this.isFixable(itemStack))
		{
			Text.getService().sendMessage(player, "fix-invalid-item");
			return;
		}
		
		CooldownAccount account = Cooldowns.getService().getAccount(player.getUniqueId());
		
		if (account.isCooldown(CommandFixHand.COOLDOWN_NAME))
		{
			Cooldown cooldown = account.getCooldown(CommandFixHand.COOLDOWN_NAME);
			Instant remaining = cooldown.getRemaining();
			Text.getService().sendMessage(player, "fix-hand-cooldown", PrisonMetadataDefaults.LAST_FIX_HAND_COOLDOWN, this.format(remaining));
			return;
		}
		
		Cooldown cooldown = Cooldown.builder()
				.setUniqueId(player.getUniqueId())
				.setName(CommandFixHand.COOLDOWN_NAME)
				.setStart(Instant.now())
				.setExpiry(this.getExpiry())
				.build();
		
		account.setCooldown(cooldown);
		
		itemStack.setDurability((short) 0);
		player.updateInventory();
		
		Text.getService().sendMessage(player, "fix-hand");
	}
	
	private Instant getExpiry()
	{
		// todo: need to talk to packet
		return Instant.now();
	}

}