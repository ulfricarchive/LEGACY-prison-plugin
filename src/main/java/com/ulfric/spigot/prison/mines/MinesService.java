package com.ulfric.spigot.prison.mines;

import com.ulfric.commons.service.Service;
import com.ulfric.commons.spigot.chunk.ChunkUtils;
import com.ulfric.commons.spigot.guard.Cuboid;
import com.ulfric.commons.spigot.guard.Guard;
import com.ulfric.commons.spigot.guard.Point;
import com.ulfric.commons.spigot.guard.Region;
import com.ulfric.commons.spigot.guard.Shape;
import com.ulfric.commons.spigot.plugin.PluginUtils;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.server.v1_11_R1.Chunk;
import net.minecraft.server.v1_11_R1.ChunkSection;
import net.minecraft.server.v1_11_R1.IBlockData;
import net.minecraft.server.v1_11_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;

public class MinesService implements Service {

	@Inject
	private Container owner;

	ExecutorService executorService = Executors.newFixedThreadPool(10);

	@Initialize
	private void initialize()
	{
	}

	private void test()
	{
		Bukkit.getScheduler().runTaskLater(PluginUtils.getAllPlugins().get(0), () ->
		{
			Point min = Point.builder().setX(0).setY(0).setZ(0).build();
			Point max = Point.builder().setX(100).setY(256).setZ(100).build();
			Shape shape = new Cuboid(min, max);
			Region region = Region.builder().setName("Mine A").setBounds(shape)
					.setWorld(Bukkit.getWorld("world").getUID()).build();
			Guard.getService().addRegion(region);
			ArrayList<MineBlock> mineBlocks = new ArrayList<>();
			mineBlocks.add(new MineBlock("GOLD_BLOCK", 1000));
			mineBlocks.add(new MineBlock("IRON_BLOCK", 1000));
			mineBlocks.add(new MineBlock("DIAMOND_BLOCK", 1000));
			Mine mine = new Mine(region.getName(), "Mine A", mineBlocks);
			Bukkit.getScheduler().runTaskTimer(PluginUtils.getAllPlugins().get(0), () ->
			{
				regenerateMine(mine);
			}, 0, 1);
		}, 200);
	}

	public void regenerateMine(Mine mine)
	{
		long start = System.currentTimeMillis();
		HashMap<ChunkCords, ChunkSection[]> chunks = new HashMap<>();
		Region region = Guard.getService().getRegion(mine.region);
		Objects.requireNonNull(region, "Region is null");
		Point min = region.getBounds().getMin();
		Point max = region.getBounds().getMax();
		World world = ((CraftWorld) Bukkit.getWorld(region.getWorld())).getHandle();
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
					chunks.put(cords, sections);
				}
				int finalX = x;
				int finalZ = z;
				executorService.submit(() ->
				{
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
						sections[y >> 4].setType(finalX & 15, y & 15, finalZ & 15, iBlockData);
					}
				});
			}
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(PluginUtils.getMainPlugin(), () ->
		{
			for (Entry<ChunkCords, ChunkSection[]> entry : chunks.entrySet())
			{
				Chunk chunk = world.getChunkAt(entry.getKey().x, entry.getKey().z);
				chunk.initLighting();
				ChunkUtils.applyChanges(chunk, entry.getValue());
			}
			System.out.println(
					"Resenting Mine: " + mine.mine + " --- " + (System.currentTimeMillis() - start) + "ms");
		});
	}

	private class ChunkCords {

		int x;
		int z;

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

		public ChunkCords(int x, int z)
		{

			this.x = x;
			this.z = z;
		}
	}
}
