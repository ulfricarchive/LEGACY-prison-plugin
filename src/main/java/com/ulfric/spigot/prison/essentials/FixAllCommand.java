package com.ulfric.spigot.prison.essentials;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import com.ulfric.commons.spigot.data.Data;
import com.ulfric.commons.spigot.data.DataSection;
import com.ulfric.commons.spigot.data.PersistentData;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.cooldown.Cooldown;
import com.ulfric.commons.spigot.cooldown.CooldownAccount;
import com.ulfric.commons.spigot.cooldown.Cooldowns;
import com.ulfric.commons.spigot.text.Text;
import com.ulfric.spigot.prison.metadata.PrisonMetadataDefaults;

@Name("all")
@Permission("fix-all-use")
@MustBePlayer
class FixAllCommand extends FixCommand {

	private static final String COOLDOWN_NAME = "COMMAND_FIX_ALL";
	
	@Inject
	private Container owner;
	
	private final Map<String, Long> cooldowns = new CaseInsensitiveMap<>();
	
	@Initialize
	private void initialize()
	{
		PersistentData data = Data.getDataStore(this.owner).getDataStore("fix-command").getDefault();
		this.loadCooldowns(data);
	}
	
	private void loadCooldowns(PersistentData data)
	{
		data.getSection("fix-all").getSections().forEach(this::loadCooldown);
	}
	
	private void loadCooldown(DataSection section)
	{
		String permission = section.getString("permission");
		long cooldown = section.getLong("cooldown");
		this.cooldowns.put(permission, cooldown);
	}

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
			
			Instant expiry = this.getExpiry(player);
			
			if (expiry != null)
			{
				Cooldown cooldown = Cooldown.builder()
						.setUniqueId(player.getUniqueId())
						.setName(FixAllCommand.COOLDOWN_NAME)
						.setStart(Instant.now())
						.setExpiry(expiry)
						.build();
				
				account.setCooldown(cooldown);
			}
			
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

	private Instant getExpiry(Player player)
	{
		long cooldown = this.identifyCooldownLength(player);
		
		if (cooldown == -1)
		{
			return null;
		}
		
		return Instant.now().plusMillis(cooldown);
	}
	
	private long identifyCooldownLength(Player player)
	{
		return this.cooldowns.keySet().
				stream().
					filter(player::hasPermission).
					collect(Collectors.toList()).
						stream().
							mapToLong(this.cooldowns::get).
							boxed().
							sorted(Collections.reverseOrder()).
							findFirst().orElse(-1L);
	}

}