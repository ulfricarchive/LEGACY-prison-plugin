package com.ulfric.spigot.prison.plots;

import com.ulfric.commons.spigot.point.PointUtils;
import com.ulfric.commons.spigot.shape.Point;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class PlotsService implements Plots, Listener {

	List<Plot> plots = new ArrayList<>();

	@Inject
	private Container owner;

	@Initialize
	private void initialize()
	{
	}

	@EventHandler
	public void test(PlayerInteractEvent event)
	{
		generate(10);
	}

	private void generate(int sideLength)
	{
		Plot plot = null;
		List<Point> bases = base();
		World world = Bukkit.getWorld("world");
		world.setAutoSave(false);
		if (plots.size() > 0)
		{
			for (Point base : bases)
			{
				if (plot != null)
				{
					break;
				}
				for (Vector direction : Plot.DIRECTIONS)
				{
					plot = new Plot(base, direction, sideLength);
					if (plot(base, direction) == null
							&& plot(plot.max(), direction.clone().multiply(-1)) == null)
					{
						break;
					}
					else
					{
						plot = null;
					}
				}
			}
		}
		else
		{
			plot = new Plot(Point.ZERO, Plot.DIRECTIONS[0], sideLength);
		}
		if (plot != null)
		{
			Vector direction = plot.getDirection();
			Point min = PointUtils.multiply(plot.getBase(), direction);
			Point max = PointUtils.multiply(plot.max(), direction);
			for (int x = min.getX(); x <= max.getX(); x++)
			{
				if (x == min.getX() || x == max.getX())
				{
					for (int z = min.getZ(); z <= max.getZ(); z++)
					{
						world.getBlockAt((int) (x * direction.getX()), 3, (int) (z * direction.getZ()))
								.setType(Material.DIAMOND_BLOCK);
					}
				}
				else
				{
					world.getBlockAt((int) (x * direction.getX()), 3, (int) (min.getZ() * direction.getZ()))
							.setType(Material.DIAMOND_BLOCK);
					world.getBlockAt((int) (x * direction.getX()), 3, (int) (max.getZ() * direction.getZ()))
							.setType(Material.DIAMOND_BLOCK);
				}
			}
			plots.add(plot);
		}
	}

	public Plot plot(Point base, Vector direction)
	{
		for (Plot plot : plots)
		{
			if (plot.getBase().equals(base) && plot.getDirection().equals(direction))
			{
				return plot;
			}
		}
		return null;
	}

	public List<Point> base()
	{
		List<Point> bases = new ArrayList<>();
		plots.stream().filter(plot -> !bases.contains(plot.getBase())).forEach(plot ->
				bases.add(plot.getBase()));
		plots.stream().filter(plot -> !bases.contains(plot.max())).forEach(plot ->
				bases.add(plot.max()));
		bases.sort((o1, o2) ->
		{
			double d1 = PointUtils.substract(Point.ZERO, o1).length();
			double d2 = PointUtils.substract(Point.ZERO, o2).length();
			return Double.compare(d1, d2);
		});
		return bases;
	}

}
