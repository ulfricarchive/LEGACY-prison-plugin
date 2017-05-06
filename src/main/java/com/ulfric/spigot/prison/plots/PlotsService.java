package com.ulfric.spigot.prison.plots;

import com.ulfric.commons.spigot.point.PointUtils;
import com.ulfric.commons.spigot.shape.Point;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.util.Vector;

public class PlotsService implements Plots {

	List<Plot> plots = new ArrayList<>();

	@Inject
	private Container owner;

	@Initialize
	private void initialize()
	{
		for (int i = 0; i < 20; i++)
		{
			generate(10);
		}
	}

	private void generate(int sideLength)
	{
		Plot plot = null;
		List<Point> bases = base();
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
					if (plot(base, direction) == null)
					{
						plot = new Plot(base, direction, sideLength);
						if (bases.contains(plot.max()))
						{
							plot = null;
						}
						else
						{
							break;
						}
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
