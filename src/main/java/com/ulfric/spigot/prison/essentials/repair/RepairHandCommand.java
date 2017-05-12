package com.ulfric.spigot.prison.essentials.repair;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Alias;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.cooldown.Cooldown;
import com.ulfric.commons.spigot.cooldown.CooldownAccount;
import com.ulfric.commons.spigot.cooldown.Cooldowns;
import com.ulfric.commons.spigot.text.Text;
import com.ulfric.commons.text.FormatUtils;
import com.ulfric.spigot.prison.metadata.PrisonMetadataDefaults;

import java.time.Instant;

@Name("hand")
@Alias({"held", "item"})
@Permission("repair-hand-use")
class RepairHandCommand extends RepairCommand {
	
	private static final String COOLDOWN_NAME = "REPAIR_HAND_COMMAND";
	private static final String BYPASS_PERMISSION_NODE = "repair-hand-cooldown-bypass";
	
	@Override
	public void run(Context context)
	{
		Player player = (Player) context.getSender();
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		
		Text text = Text.getService();
		
		if (!this.isFixable(itemStack))
		{
			text.sendMessage(player, "repair-invalid-item");
			return;
		}
		
		CooldownAccount account = Cooldowns.getService().getAccount(player.getUniqueId());
		
		boolean bypass = player.hasPermission(RepairHandCommand.BYPASS_PERMISSION_NODE);
		
		if (account.isCooldown(RepairHandCommand.COOLDOWN_NAME) && !bypass)
		{
			Cooldown cooldown = account.getCooldown(RepairHandCommand.COOLDOWN_NAME);
			Instant remaining = cooldown.getRemaining();
			text.sendMessage(player, "repair-hand-cooldown",
					PrisonMetadataDefaults.LAST_FIX_HAND_COOLDOWN, FormatUtils.formatRemaining(remaining.toEpochMilli()));
			return;
		}
		
		if (!bypass)
		{
			long length = RepairService.getService().getCooldownLength(RepairService.RepairType.HAND, player);
			
			if (length != -1)
			{
				Cooldown cooldown = Cooldown.builder()
						.setName(RepairHandCommand.COOLDOWN_NAME)
						.setUniqueId(player.getUniqueId())
						.setExpiry(Instant.now().plusMillis(length))
						.build();
				
				account.setCooldown(cooldown);
			}
		}
		
		this.performRepair(player, itemStack);
	}
	
	private void performRepair(Player player, ItemStack itemStack)
	{
		itemStack.setDurability((short) 0);
		player.updateInventory();
		Text.getService().sendMessage(player, "repair-hand");
	}
	
}
