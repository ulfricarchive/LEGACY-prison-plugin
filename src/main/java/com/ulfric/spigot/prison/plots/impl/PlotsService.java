package com.ulfric.spigot.prison.plots.impl;

import com.ulfric.commons.service.Service;
import com.ulfric.commons.spigot.point.PointUtils;
import com.ulfric.commons.spigot.service.ServiceUtils;
import com.ulfric.commons.spigot.shape.Point;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;
import com.ulfric.spigot.prison.plots.Plot;
import com.ulfric.spigot.prison.plots.Plots;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import org.bukkit.util.Vector;

public class PlotsService implements Plots, Service {

	@Inject
	Container container;
	private ConcurrentMap<UUID, Set<Plot>> mappedPlots = new ConcurrentHashMap<>();
	private Set<Point> failedBases = ConcurrentHashMap.newKeySet();

	public static Plots getService()
	{
		return ServiceUtils.getService(PlotsService.class);
	}

	@Initialize
	private void initialize()
	{
		loadPlots();
	}

	@Override
	public void loadPlots()
	{
		//TODO deserialize plots
	}

	@Override
	public void savePlots()
	{
		getPlotList().stream().filter(Objects::nonNull).forEach(this::savePlot);
	}

	private void savePlot(Plot plot)
	{
		//TODO save the json to a file
		System.out.println("-----------");
		System.out.println(plot.getBase());
		System.out.println(plot.getDirection());
		System.out.println(plot.getUuid());
		System.out.println("-----------");
	}

	private void loadPlot(UUID owner)
	{
		//TODO load the json from a file
	}

	@Override
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
				if (base != null)
				{
					if (plot != null)
					{
						break;
					}
					for (Vector direction : PlotConfig.DIRECTIONS)
					{
						plot = new TestPlot(base, direction);
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
		}
		else
		{
			plot = new TestPlot(Point.ZERO, PlotConfig.DIRECTIONS[0]);
		}
		if (plot != null)
		{
			addPlot(new PlayerPlot(owner, plot.getBase(), plot.getDirection()));
		}
		System.out.println((System.currentTimeMillis() - start) + " --- " + plots.size());
		return plot;
	}

	private void addPlot(Plot plot)
	{
		Set<Plot> ownerPlots = new HashSet<>();
		UUID owner = null;
		if (plot instanceof PlayerPlot)
		{
			PlayerPlot playerPlot = (PlayerPlot) plot;
			owner = playerPlot.getOwner();
			if (mappedPlots.containsKey(owner))
			{
				ownerPlots = mappedPlots.get(owner);
			}
		}
		ownerPlots.add(plot);
		mappedPlots.put(owner, ownerPlots);
	}

	@Override
	public boolean checkBaseDir(Plot plot, Plot plot1)
	{
		return plot1.getBase().equals(plot.getBase()) && plot1.getDirection()
				.equals(plot.getDirection());
	}

	@Override
	public boolean checkCombinations(Plot plot)
	{
		Set<Plot> plots = getPlotList();
		Plot plotX = new TestPlot(plot.getFurthestX(),
				plot.getDirection().clone().multiply(new Vector(-1, 0, 1)));
		Plot plotZ = new TestPlot(plot.getFurthestZ(),
				plot.getDirection().clone().multiply(new Vector(1, 0, -1)));
		Plot plotXZ = new TestPlot(plot.getFurthestXZ(),
				plot.getDirection().clone().multiply(-1));
		return !plots.parallelStream().anyMatch(
				plot1 -> checkBaseDir(plot, plot1) || checkBaseDir(plotX, plot1) || checkBaseDir(plotZ,
						plot1) || checkBaseDir(plotXZ, plot1));
	}

	@Override
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

	@Override
	public Plot getPlotByUUID(UUID uuid)
	{
		Set<Plot> plots = getPlotList();
		Optional<Plot> optional = plots.stream().filter(plot -> plot.getUuid().equals(uuid))
				.findFirst();
		if (optional.isPresent())
		{
			return optional.get();
		}
		return null;
	}

	@Override
	public Set<Plot> getPlotByOwner(UUID owner)
	{
		return mappedPlots.get(owner);
	}

	@Override
	public Set<Point> sortPlotsByRadius(Point center, Set<Plot> plots)
	{
		Set<Point> bases = ConcurrentHashMap.newKeySet();
		plots.parallelStream().filter(Objects::nonNull)
				.filter(plot -> !(failedBases.contains(plot.getBase()) && failedBases
						.contains(plot.getFurthestXZ()))).forEach(plot ->
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
		bases.parallelStream().filter(Objects::nonNull).collect(Collectors.toList()).sort((o1, o2) ->
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
