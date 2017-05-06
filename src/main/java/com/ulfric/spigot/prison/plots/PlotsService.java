package com.ulfric.spigot.prison.plots;

import com.ulfric.commons.spigot.shape.Point;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;
import java.util.TreeMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;

public class PlotsService implements Plots {

	TreeMap<Integer, Plot> plots = new TreeMap<>();

	@Inject
	private Container owner;

	@Initialize
	private void initialize()
	{
		generate(10);
	}

	private void generate(int sideLength)
	{
		World world = Bukkit.getWorld("world");
		Point base = null;
		Point max = null;
		if (plots.size() == 0)
		{
			Plot plot = new Plot(Point.ZERO, Plot.DIRECTIONS[0], sideLength);
			plots.put(0, plot);
			base = plot.getBase();
			max = plot.max();
		}
		if (base != null && max != null)
		{
			world.getBlockAt(base.getX(), base.getY(), base.getZ()).setType(Material.DIAMOND_BLOCK);
			world.getBlockAt(max.getX(), max.getY(), max.getZ()).setType(Material.GOLD_BLOCK);
		}
	}

}
