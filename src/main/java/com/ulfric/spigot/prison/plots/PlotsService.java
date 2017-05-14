package com.ulfric.spigot.prison.plots;

import com.ulfric.commons.spigot.plugin.PluginUtils;
import com.ulfric.commons.spigot.point.PointUtils;
import com.ulfric.commons.spigot.shape.Point;
import com.ulfric.dragoon.initialize.Initialize;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PlotsService implements Plots, Listener {

	private ConcurrentMap<UUID, Set<Plot>> mappedPlots = new ConcurrentHashMap<>();

	private Set<Point> failedBases = ConcurrentHashMap.newKeySet();

	@Initialize
	private void initialize()
	{
		//loadplots
		Bukkit.getScheduler().runTaskTimerAsynchronously(PluginUtils.getMainPlugin(), () ->
				generatePlot(UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5")), 0, 0);
	}

	public Plot generatePlot(UUID owner)
	{
		long start = System.currentTimeMillis();
		Set<Plot> plots = getPlotList();
		Set<Point> bases = sortPlotsByRadius(Point.ZERO, plots);
		Plot plot = null;
		if (plots.size() > 0)
		{
			for (Point base : bases)
			{
				if (plot != null)
				{
					break;
				}
				for (Vector direction : PlotConfig.DIRECTIONS)
				{
					plot = new Plot(owner, base, direction);
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
			plot = new Plot(owner, Point.ZERO, PlotConfig.DIRECTIONS[0]);
		}
		if (plot != null)
		{
			Set<Plot> ownerPlots = new HashSet<>();
			if (mappedPlots.containsKey(owner))
			{
				ownerPlots = mappedPlots.get(owner);
			}
			ownerPlots.add(plot);
			mappedPlots.put(owner, ownerPlots);
		}
		System.out.println((System.currentTimeMillis() - start) + " --- " + plots.size());
		return plot;
	}

	public boolean checkBaseDir(Plot plot, Plot plot1)
	{
		return plot1.getBase().equals(plot.getBase()) && plot1.getDirection()
				.equals(plot.getDirection());
	}

	public boolean checkCombinations(Plot plot)
	{
		Set<Plot> plots = getPlotList();
		Plot plotX = new Plot(plot.getFurthestX(),
				plot.getDirection().clone().multiply(new Vector(-1, 0, 1)));
		Plot plotZ = new Plot(plot.getFurthestZ(),
				plot.getDirection().clone().multiply(new Vector(1, 0, -1)));
		Plot plotXZ = new Plot(plot.getFurthestXZ(),
				plot.getDirection().clone().multiply(-1));
		return !(plots.contains(plot) || plots.contains(plotX) || plots.contains(plotZ) || plots.contains(plotXZ));
	}

	public Plot getPlotByBaseDir(Point base, Vector direction)
	{
		Set<Plot> plots = getPlotList();
		for (Plot plot : plots)
		{
			if (plot.getBase().equals(base) && plot.getDirection().equals(direction))
			{
				return plot;
			}
		}
		return null;
	}

	public Plot getPlotByUUID(UUID uuid)
	{
		Set<Plot> plots = getPlotList();
		return plots.stream().filter(plot -> plot.getUuid().equals(uuid)).findFirst().get();
	}

	public Set<Plot> getPlotByOwner(UUID owner)
	{
		return mappedPlots.get(owner);
	}

	public Set<Point> sortPlotsByRadius(Point center, Set<Plot> plots)
	{
		List<Point> bases = new ArrayList<>();
		plots.stream().filter(plot -> !(failedBases.contains(plot.getBase()) && failedBases
				.contains(plot.getFurthestXZ()))).forEachOrdered(plot ->
		{
			if (!bases.contains(plot.getBase()))
			{
				bases.add(plot.getBase());
			}
			if (!bases.contains(plot.getFurthestXZ()))
			{
				bases.add(plot.getFurthestXZ());
			}
		});
		bases.sort((o1, o2) ->
		{
			double d1 = PointUtils.substract(center, o1).length();
			double d2 = PointUtils.substract(center, o2).length();
			return Double.compare(d1, d2);
		});
		LinkedHashSet<Point> set = new LinkedHashSet<>();
		set.addAll(bases);
		return set;
	}

	private Set<Plot> getPlotList()
	{
		Set<Plot> plots = new HashSet<>();
		mappedPlots.values().forEach(plots::addAll);
		return plots;
	}

}
