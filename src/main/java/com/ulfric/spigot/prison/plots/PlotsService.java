package com.ulfric.spigot.prison.plots;

import com.ulfric.commons.spigot.point.PointUtils;
import com.ulfric.commons.spigot.shape.Point;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class PlotsService implements Plots, Listener {

	private List<Plot> plots = new CopyOnWriteArrayList<>();

	@Inject
	private Container owner;

	@Initialize
	private void initialize()
	{
		World world = Bukkit.getWorld("world");
		world.setAutoSave(false);
	}

	@EventHandler
	public void test(PlayerInteractEvent event)
	{
		plots.forEach(this::outline);
	}

	private void outline(Plot plot)
	{
		World world = Bukkit.getWorld("world");
		Vector direction = plot.getDirection();
		Point min = PointUtils.multiply(plot.getBase(), direction);
		Point max = PointUtils.multiply(plot.getFurthestXZ(), direction);
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
	}

	private HashSet<Point> failedBases = new HashSet<>();

	private Plot generatePlot(int sideLength)
	{
		Plot plot = null;
		List<Point> bases = sortPlotsByRadius(Point.ZERO, plots);
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
					if (checkCombinations(plot))
					{
						break;
					}
					else
					{
						plot = null;
					}
				}
				if (plot == null)
				{
					failedBases.add(base);
				}
			}
		}
		else
		{
			plot = new Plot(Point.ZERO, Plot.DIRECTIONS[0], sideLength);
		}
		return plot;
	}

	private boolean checkBaseDir(Plot plot, Plot plot1)
	{
		return plot1.getBase().equals(plot.getBase()) && plot1.getDirection()
				.equals(plot.getDirection());
	}

	private boolean checkCombinations(Plot plot)
	{
		Plot plotX = new Plot(plot.getFurthestX(),
				plot.getDirection().clone().multiply(new Vector(-1, 0, 1)), plot.getSideLength());
		Plot plotZ = new Plot(plot.getFurthestZ(),
				plot.getDirection().clone().multiply(new Vector(1, 0, -1)), plot.getSideLength());
		Plot plotXZ = new Plot(plot.getFurthestXZ(), plot.getDirection().clone().multiply(-1),
				plot.getSideLength());
		for (Plot plot1 : plots)
		{
			if (checkBaseDir(plot1, plot) || checkBaseDir(plot1, plotX) || checkBaseDir(plot1, plotZ)
					|| checkBaseDir(plot1, plotXZ))
			{
				return false;
			}
		}
		return true;
	}

	public Plot getPlot(Point base, Vector direction)
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

	private List<Point> sortPlotsByRadius(Point center, List<Plot> plots)
	{
		List<Point> bases = new ArrayList<>();
		for (Plot plot : plots)
		{
			if (!(failedBases.contains(plot.getBase()) &&failedBases.contains(plot.getFurthestXZ()))){
				if (!bases.contains(plot.getBase()))
				{
					bases.add(plot.getBase());
				}
				if (!bases.contains(plot.getFurthestXZ()))
				{
					bases.add(plot.getFurthestXZ());
				}
			}
		}
		bases.sort((o1, o2) ->
		{
			double d1 = PointUtils.substract(center, o1).length();
			double d2 = PointUtils.substract(center, o2).length();
			return Double.compare(d1, d2);
		});
		return bases;
	}

}
