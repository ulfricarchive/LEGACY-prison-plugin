package com.ulfric.spigot.prison.essentials.repair;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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

@Name("all")
@Alias("inventory")
@Permission("repair-all-use")
class RepairAllCommand extends RepairCommand {
	
	private static final String COOLDOWN_NAME = "REPAIR_ALL_COMMAND";
	private static final String BYPASS_PERMISSION_NODE = "repair-all-cooldown-bypass";
	
	@Override
	public void run(Context context)
	{
		Player player = (Player) context.getSender();
		
		CooldownAccount account = Cooldowns.getService().getAccount(player.getUniqueId());
		Text text = Text.getService();
		
		boolean bypass = player.hasPermission(RepairAllCommand.BYPASS_PERMISSION_NODE);
		
		if (account.isCooldown(RepairAllCommand.COOLDOWN_NAME) && !bypass)
		{
			Cooldown cooldown = account.getCooldown(RepairAllCommand.COOLDOWN_NAME);
			Instant remaining = cooldown.getRemaining();
			text.sendMessage(player, "repair-all-cooldown",
					PrisonMetadataDefaults.LAST_FIX_ALL_COOLDOWN, FormatUtils.formatRemaining(remaining.toEpochMilli()));
			return;
		}
		
		boolean repaired = this.performInventoryRepair(player.getInventory());
		
		if (repaired)
		{
			player.updateInventory();
			
			if (!bypass)
			{
				long length = RepairService.getService().getCooldownLength(RepairService.RepairType.ALL, player);
				
				if (length == -1)
				{
					return;
				}
				
				Cooldown cooldown = Cooldown.builder()
						.setName(RepairAllCommand.COOLDOWN_NAME)
						.setUniqueId(player.getUniqueId())
						.setExpiry(Instant.now().plusMillis(length))
						.build();
				
				account.setCooldown(cooldown);
			}
			
			text.sendMessage(player, "repair-all");
			return;
		}
		
		text.sendMessage(player, "repair-all-no-items-to-fix");
	}
	
	private boolean performInventoryRepair(Inventory inventory)
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
	
}
