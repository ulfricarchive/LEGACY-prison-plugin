package com.ulfric.spigot.prison.essentials;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import com.ulfric.commons.spigot.data.Data;
import com.ulfric.commons.spigot.data.DataSection;
import com.ulfric.commons.spigot.data.PersistentData;
import com.ulfric.commons.spigot.economy.BalanceDeductionResult;
import com.ulfric.commons.spigot.economy.BankAccount;
import com.ulfric.commons.spigot.economy.CurrencyAmount;
import com.ulfric.commons.spigot.economy.Economy;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.bukkit.entity.Player;
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

@Name("hand")
@Permission("fix-hand-use")
@MustBePlayer
class FixHandCommand extends FixCommand {
	
	private static final String COOLDOWN_NAME = "COMMAND_FIX_HAND";
	
	@Inject
	private Container owner;
	
	private final Map<String, Long> cooldowns = new CaseInsensitiveMap<>();
	private CurrencyAmount price;
	
	@Initialize
	private void initialize()
	{
		PersistentData data = Data.getDataStore(this.owner).getDataStore("fix-command").getDefault();
		this.loadSettings(data);
	}
	
	private void loadSettings(PersistentData data)
	{
		DataSection section = data.getSection("fix-hand");
		
		section.getSection("cooldowns").getSections().forEach(this::loadCooldown);
		
		DataSection settings = section.getSection("settings");
		this.price = CurrencyAmount.parseCurrencyAmount(settings.getString("price"));
	}
	
	private void loadCooldowns(PersistentData data)
	{
		data.getSection("fix-hand").getSection("cooldowns").getSections().forEach(this::loadCooldown);
	}
	
	private void loadCooldown(DataSection section)
	{
		String permission = section.getString("permission");
		long cooldown = section.getLong("cooldown");
		this.cooldowns.putIfAbsent(permission, cooldown);
	}
	
	@Override
	public void run(Context context)
	{
		Player player = (Player) context.getSender();
		Text text = Text.getService();
		
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		
		if (!this.isFixable(itemStack))
		{
			text.sendMessage(player, "fix-invalid-item");
			return;
		}
		
		CooldownAccount account = Cooldowns.getService().getAccount(player.getUniqueId());
		
		if (account.isCooldown(FixHandCommand.COOLDOWN_NAME))
		{
			Cooldown cooldown = account.getCooldown(FixHandCommand.COOLDOWN_NAME);
			Instant remaining = cooldown.getRemaining();
			text.sendMessage(player, "fix-hand-cooldown", PrisonMetadataDefaults.LAST_FIX_HAND_COOLDOWN, this.format(remaining));
			return;
		}
		
		Instant expiry = this.getExpiry(player);
		
		if (expiry == null)
		{
			if (this.price == null)
			{
				text.sendMessage(player, "fix-hand-fail");
			}
			else if (player.hasPermission("fix-hand-bypass"))
			{
				this.performFix(player, itemStack);
			}
			else
			{
				BankAccount bankAccount = Economy.getService().getAccount(player.getUniqueId());
				BalanceDeductionResult result = bankAccount.deduct(this.price);
				
				if (result.isSuccess())
				{
					this.performFix(player, itemStack);
				}
				else
				{
					text.sendMessage(player, "fix-hand-cannot-afford");
				}
			}
			
			return;
		}
		
		Cooldown cooldown = Cooldown.builder()
				.setUniqueId(player.getUniqueId())
				.setName(FixHandCommand.COOLDOWN_NAME)
				.setStart(Instant.now())
				.setExpiry(expiry)
				.build();
		
		account.setCooldown(cooldown);
		
		this.performFix(player, itemStack);
	}
	
	private void performFix(Player player, ItemStack itemStack)
	{
		itemStack.setDurability((short) 0);
		player.updateInventory();
		
		Text.getService().sendMessage(player, "fix-hand");
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