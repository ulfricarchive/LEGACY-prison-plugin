package com.ulfric.spigot.prison.plots;

import com.google.common.collect.Iterables;
import com.google.gson.GsonBuilder;
import com.ulfric.commons.spigot.point.PointUtils;
import com.ulfric.commons.spigot.shape.Point;
import com.ulfric.dragoon.initialize.Initialize;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class PlotsService implements Plots, Listener {

	private ConcurrentHashMap<UUID, Set<Plot>> mappedPlots = new ConcurrentHashMap<>();

	private HashSet<Point> failedBases = new HashSet<>();

	@Initialize
	private void initialize()
	{
		System.out.println(
				new GsonBuilder().setPrettyPrinting().create().toJson(generatePlot(UUID.randomUUID())));
	}

	public Plot generatePlot(UUID owner)
	{
		List<Plot> plots = getPlotList();
		List<Point> bases = sortPlotsByRadius(Point.ZERO, plots);
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
			if (mappedPlots.contains(owner))
			{
				ownerPlots = mappedPlots.get(owner);
			}
			ownerPlots.add(plot);
			mappedPlots.put(owner, ownerPlots);
		}
		return plot;
	}

	public boolean checkBaseDir(Plot plot, Plot plot1)
	{
		return plot1.getBase().equals(plot.getBase()) && plot1.getDirection()
				.equals(plot.getDirection());
	}

	public boolean checkCombinations(Plot plot)
	{
		List<Plot> plots = getPlotList();
		Plot plotX = new Plot(plot.getOwner(), plot.getFurthestX(),
				plot.getDirection().clone().multiply(new Vector(-1, 0, 1)));
		Plot plotZ = new Plot(plot.getOwner(), plot.getFurthestZ(),
				plot.getDirection().clone().multiply(new Vector(1, 0, -1)));
		Plot plotXZ = new Plot(plot.getOwner(), plot.getFurthestXZ(),
				plot.getDirection().clone().multiply(-1));
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

	public Plot getPlotByBaseDir(Point base, Vector direction)
	{
		List<Plot> plots = getPlotList();
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
		List<Plot> plots = getPlotList();
		return plots.stream().filter(plot -> plot.getUuid().equals(uuid)).findFirst().get();
	}

	public Set<Plot> getPlotByOwner(UUID owner)
	{
		return mappedPlots.get(owner);
	}

	public List<Point> sortPlotsByRadius(Point center, List<Plot> plots)
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
		return bases;
	}

	private List<Plot> getPlotList()
	{
		List<Plot> plots = new ArrayList<>();
		for (Set<Plot> plotSet : mappedPlots.values())
		{
			Iterables.concat(plots, plotSet);
		}
		return plots;
	}

}
