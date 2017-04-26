package com.ulfric.spigot.prison.mines;

import com.ulfric.commons.service.Service;
import com.ulfric.commons.spigot.chunk.ChunkUtils;
import com.ulfric.commons.spigot.data.Data;
import com.ulfric.commons.spigot.data.DataStore;
import com.ulfric.commons.spigot.data.PersistentData;
import com.ulfric.commons.spigot.guard.Guard;
import com.ulfric.commons.spigot.guard.Point;
import com.ulfric.commons.spigot.guard.Region;
import com.ulfric.commons.spigot.plugin.PluginUtils;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import net.minecraft.server.v1_11_R1.Chunk;
import net.minecraft.server.v1_11_R1.ChunkSection;
import net.minecraft.server.v1_11_R1.IBlockData;
import net.minecraft.server.v1_11_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;

public class MinesService implements Service {

	List<Mine> mines = new ArrayList<>();
	ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(30);

	@Inject
	private Container owner;

	@Initialize
	private void initialize()
	{
		load();
		autoRegen();
	}

	private void autoRegen()
	{
		//come up with some auto regen method
		//maybe every 5 mins
	}

	private void load()
	{
		DataStore dataStore = Data.getDataStore(owner);
		dataStore.loadAllData().forEach(persistentData ->
		{
			String mine = persistentData.getName();
			String region = persistentData.getString("Region");
			List<String> blocks = persistentData.getStringList("Blocks");
			List<MineBlock> mineBlocks = new ArrayList<>();
			for (String block : blocks)
			{
				String material = block.split(":")[0];
				int wright = Integer.parseInt(block.split(":")[1]);
				mineBlocks.add(new MineBlock(material, wright));
			}
			mines.add(new Mine(region, mine, mineBlocks));
		});
	}

	private void save()
	{
		DataStore dataStore = Data.getDataStore(owner);
		for (Mine mine : mines)
		{
			PersistentData data = dataStore.getData(mine.mine);
			data.set("Region", mine.region);
			List<String> blocks = new ArrayList<>();
			for (MineBlock mineBlock : mine.mineBlocks)
			{
				blocks.add(mineBlock.material + ":" + mineBlock.weight);
			}
			data.set("Blocks", blocks);
			data.save();
		}
	}

	public void regenerateMine(Mine mine)
	{
		long start = System.currentTimeMillis();
		Region region = Guard.getService().getRegion(mine.region);
		Objects.requireNonNull(region, "Region is null");
		Point min = region.getBounds().getMin();
		Point max = region.getBounds().getMax();
		World world = ((CraftWorld) Bukkit.getWorld(region.getWorld())).getHandle();
		Map<ChunkCords, ChunkSection[]> chunks = getChunks(world, min, max);
		executorService.submit(() ->
		{
			for (int x = min.getX(); x <= max.getX(); x++)
			{
				for (int z = min.getZ(); z <= max.getZ(); z++)
				{
					ChunkSection[] sections = chunks.get(new ChunkCords(x >> 4, z >> 4));
					for (int y = min.getY(); y <= max.getY() && y < 256; y++)
					{
						MineBlock mineBlock = mine.getNextBLock();
						Objects.requireNonNull(mineBlock, "mineBLock is null");
						IBlockData iBlockData = ChunkUtils
								.nmsBlock(Material.getMaterial(mineBlock.material), (byte) 0);
						if (sections[y >> 4] == null)
						{
							sections[y >> 4] = new ChunkSection(y >> 4 << 4, true);
						}
						sections[y >> 4].setType(x & 15, y & 15, z & 15, iBlockData);
					}
				}
			}
			Bukkit.getScheduler().runTask(PluginUtils.getMainPlugin(), () ->
			{
				for (Entry<ChunkCords, ChunkSection[]> entry : chunks.entrySet())
				{
					Chunk chunk = world.getChunkAt(entry.getKey().x, entry.getKey().z);
					chunk.initLighting();
					ChunkUtils.applyChanges(chunk, entry.getValue());
				}
				System.out.println(
						"Resenting Mine: " + mine.mine + " --- " + (System.currentTimeMillis() - start)
								+ "ms");
			});
		});
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
				} else
				{
					sections = world.getChunkAt(cords.x, cords.z).getSections();
				}
				chunks.put(cords, sections);
			}
		}
		return chunks;
	}

	private class ChunkCords {

		int x;
		int z;

		public ChunkCords(int x, int z)
		{

			this.x = x;
			this.z = z;
		}

		@Override
		public boolean equals(Object o)
		{
			if (this == o)
			{
				return true;
			}
			if (o == null || getClass() != o.getClass())
			{
				return false;
			}

			ChunkCords that = (ChunkCords) o;

			if (x != that.x)
			{
				return false;
			}
			return z == that.z;

		}

		@Override
		public int hashCode()
		{
			int result = x;
			result = 31 * result + z;
			return result;
		}
	}
}
