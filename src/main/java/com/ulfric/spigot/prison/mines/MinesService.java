package com.ulfric.spigot.prison.mines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.plugin.Plugin;

import com.ulfric.commons.bean.Bean;
import com.ulfric.commons.spigot.chunk.ChunkUtils;
import com.ulfric.commons.spigot.data.Data;
import com.ulfric.commons.spigot.data.DataStore;
import com.ulfric.commons.spigot.guard.Guard;
import com.ulfric.commons.spigot.guard.Region;
import com.ulfric.commons.spigot.plugin.PluginUtils;
import com.ulfric.commons.spigot.shape.Point;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;

import net.minecraft.server.v1_11_R1.Chunk;
import net.minecraft.server.v1_11_R1.ChunkSection;
import net.minecraft.server.v1_11_R1.IBlockData;
import net.minecraft.server.v1_11_R1.World;

final class MinesService implements Mines {

	private final List<Mine> mines = new ArrayList<>();
	private final ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors
			.newFixedThreadPool(30);
	private final Plugin plugin = PluginUtils.getMainPlugin();

	@Inject
	private Container owner;
	private DataStore folder;

	@Initialize
	private void initialize()
	{
		this.folder = Data.getDataStore(this.owner).getDataStore("mines");
		this.load();
//		this.autoRegen();
	}

	private void load()
	{
		this.folder.loadAllData().forEach(persistentData ->
		{
			String name = persistentData.getName();
			String region = persistentData.getString("Region");
			List<MineBlock> mineBlocks = new ArrayList<>();

			persistentData.getStringList("Blocks").forEach(block ->
			{
				String material = block.split(":")[0];
				int wright = Integer.parseInt(block.split(":")[1]);
				mineBlocks.add(new MineBlock(material, wright));
			});

			Mine mine = Mine.builder()
					.setName(name)
					.setRegion(region)
					.setMineBlocks(mineBlocks)
					.build();

			this.mines.add(mine);
		});
	}

	private void autoRegen()
	{
		Bukkit.getScheduler()
				.runTaskTimerAsynchronously(this.plugin, () -> this.getMines().forEach(this::regenerate), 0,
						0);
	}

	@Override
	public void regenerate(Mine mine)
	{
		Region region = Guard.getService().getRegion(mine.getRegion());
		Objects.requireNonNull(region, "Region is null");

		Point min = region.getBounds().getMin();
		Point max = region.getBounds().getMax();

		Bukkit.getScheduler().runTask(this.plugin, () ->
		{
			World world = ((CraftWorld) Bukkit.getWorld(region.getWorld())).getHandle();
			Map<ChunkCords, ChunkSection[]> chunks = this.getChunks(world, min, max);

			this.executorService.submit(() ->
			{
				for (int x = min.getX(); x <= max.getX(); x++)
				{

					for (int z = min.getZ(); z <= max.getZ(); z++)
					{

						ChunkSection[] sections = chunks.get(new ChunkCords(x >> 4, z >> 4));

						for (int y = min.getY(); y <= max.getY() && y < 256; y++)
						{

							MineBlock mineBlock = mine.getNextBLock();
							Objects.requireNonNull(mineBlock, "MineBlock is null");

							IBlockData iBlockData = ChunkUtils
									.nmsBlock(Material.getMaterial(mineBlock.getMaterial()), (byte) 0);

							if (sections[y >> 4] == null)
							{
								sections[y >> 4] = new ChunkSection(y >> 4 << 4, true);
							}

							sections[y >> 4].setType(x & 15, y & 15, z & 15, iBlockData);
						}

					}

				}

				Bukkit.getScheduler().runTask(this.plugin, () ->
				{
					for (Entry<ChunkCords, ChunkSection[]> entry : chunks.entrySet())
					{
						Chunk chunk = world.getChunkAt(entry.getKey().x, entry.getKey().z);
						ChunkUtils.applyChanges(chunk, entry.getValue());
					}
				});

			});

		});
	}

	@Override
	public Stream<Mine> getMines()
	{
		return new ArrayList<>(this.mines).stream();
	}

	private Map<ChunkCords, ChunkSection[]> getChunks(World world, Point min, Point max)
	{
		Map<ChunkCords, ChunkSection[]> chunks = new HashMap<>();
		for (int x = min.getX(); x <= max.getX(); x++)
		{
			for (int z = min.getZ(); z <= max.getZ(); z++)
			{
				ChunkCords cords = new ChunkCords(x >> 4, z >> 4);
				ChunkSection[] sections;
				if (chunks.containsKey(cords))
				{
					sections = chunks.get(cords);
				}
				else
				{
					sections = world.getChunkAt(cords.x, cords.z).getSections();
				}
				chunks.put(cords, sections);
			}
		}
		return chunks;
	}

	private final class ChunkCords extends Bean {

		private final int x;
		private final int z;

		ChunkCords(int x, int z)
		{

			this.x = x;
			this.z = z;
		}

	}

}
