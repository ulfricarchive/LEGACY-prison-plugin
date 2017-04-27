package com.ulfric.spigot.prison.spawner;

import org.bukkit.command.CommandSender;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.metadata.Metadata;
import com.ulfric.commons.spigot.text.placeholder.Placeholder;
import com.ulfric.spigot.prison.metadata.PrisonMetadataDefaults;

@Name("LAST_SPAWNER_TYPE")
public class LastSpawnerTypePlaceholder implements Placeholder {

	@Override
	public String apply(CommandSender to)
	{
		return Metadata.readString(to, PrisonMetadataDefaults.LAST_SPAWNER_TYPE);
	}

}