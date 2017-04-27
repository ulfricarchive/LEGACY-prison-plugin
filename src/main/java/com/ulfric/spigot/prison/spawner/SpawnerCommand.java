package com.ulfric.spigot.prison.spawner;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.command.argument.Argument;
import com.ulfric.commons.spigot.entity.LivingEntityUtils;
import com.ulfric.commons.spigot.event.Events;
import com.ulfric.commons.spigot.text.Text;
import com.ulfric.spigot.prison.metadata.PrisonMetadataDefaults;

@Name("spawner")
@Permission("spawner-use")
@MustBePlayer
public class SpawnerCommand implements Command {

	@Argument
	private EntityType type;

	@Override
	public void run(Context context)
	{
		Player player = (Player) context.getSender();
		EntityType type = this.type;

		if (!type.isSpawnable())
		{
			this.memssage(player, "spawner-not-spawnable");
			return;
		}

		if (!type.isAlive())
		{
			this.memssage(player, "spawner-not-alive");
			return;
		}

		if (!player.hasPermission("spawner-type-" + type.name().toLowerCase()))
		{
			this.memssage(player, "spawner-no-permission-for-type");
			return;
		}

		Block block = LivingEntityUtils.getSelectedBlock(player);
		if (block == null)
		{
			this.memssage(player, "spawner-no-block-selected");
			return;
		}

		BlockState state = block.getState();
		if (!(state instanceof CreatureSpawner))
		{
			this.memssage(player, "spawner-block-selected-is-not-spawner");
			return;
		}

		CreatureSpawner spawner = (CreatureSpawner) state;
		if (spawner.getSpawnedType() == type)
		{
			this.memssage(player, "spawner-already-type");
			return;
		}

		PlayerChangeSpawnerTypeEvent event = new PlayerChangeSpawnerTypeEvent(player, spawner, type);
		if (Events.fire(event).isCancelled())
		{
			return;
		}

		spawner.setSpawnedType(type);
		spawner.update();
		// TODO implement cooldown or resend the block packet without triggering the spawner?
	}

	private void memssage(Player to, String message)
	{
		Text.getService().sendMessage(to, message,
				PrisonMetadataDefaults.LAST_SPAWNER_TYPE, this.getFriendlyName());
	}

	@SuppressWarnings("deprecation")
	private String getFriendlyName()
	{
		return this.type.getName();
	}

}