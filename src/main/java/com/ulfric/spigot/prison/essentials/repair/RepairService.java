package com.ulfric.spigot.prison.essentials.repair;

import org.apache.commons.collections4.map.CaseInsensitiveMap;

import org.bukkit.entity.Player;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.service.Service;
import com.ulfric.commons.spigot.data.Data;
import com.ulfric.commons.spigot.data.DataSection;
import com.ulfric.commons.spigot.data.PersistentData;
import com.ulfric.commons.spigot.service.ServiceUtils;
import com.ulfric.commons.version.Version;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

@Name("Repair")
@Version(1)
final class RepairService implements Service {
	
	public static RepairService getService()
	{
		return ServiceUtils.getService(RepairService.class);
	}
	
	@Inject
	private Container owner;
	
	private final Map<RepairType, Map<String, Long>> cooldownLengths = new EnumMap<>(RepairType.class);
	
	@Initialize
	private void initialize()
	{
		PersistentData data = Data.getDataStore(this.owner).getDefault();
		this.loadRepairType(data);
	}
	
	private void loadRepairType(PersistentData data)
	{
		for (RepairType type : RepairType.values())
		{
			this.loadCooldowns(type, data.getSection(type.getFriendlyName()).getSections());
		}
	}
	
	private void loadCooldowns(RepairType type, Collection<DataSection> data)
	{
		Map<String, Long> cooldownLengths = new CaseInsensitiveMap<>();
		
		data.forEach(section ->
		{
			String permission = section.getString("permission");
			long length = section.getLong("cooldown");
			cooldownLengths.put(permission, length);
		});
		
		this.cooldownLengths.put(type, cooldownLengths);
	}
	
	public long getCooldownLength(RepairType type, Player player)
	{
		Map<String, Long> lengths = this.cooldownLengths.get(type);
		
		return lengths.keySet().stream()
				.filter(player::hasPermission)
				.mapToLong(lengths::get)
				.boxed()
				.sorted(Collections.reverseOrder())
				.findFirst().orElse(-1L);
	}
	
	enum RepairType
	{
		HAND("hand"),
		ALL("all");
		
		private final String friendlyName;
		
		RepairType(String friendlyName)
		{
			this.friendlyName = friendlyName;
		}
		
		public String getFriendlyName()
		{
			return this.friendlyName;
		}
		
	}
	
}
