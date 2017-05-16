package com.ulfric.spigot.prison.plots.impl;

import com.google.gson.annotations.Expose;
import com.ulfric.commons.spigot.guard.Region;
import com.ulfric.commons.spigot.point.PointUtils;
import com.ulfric.commons.spigot.shape.Point;
import com.ulfric.spigot.prison.plots.Plot;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.util.Vector;

public class TestPlot implements Plot {

	@Expose
	private final Point base;
	@Expose
	private final Vector direction;

	public TestPlot(Point base, Vector direction)
	{
		this.base = base;
		this.direction = direction;
	}

	@Override
	public Region getRegion()
	{
		return null;
	}

	@Override
	public UUID getUuid()
	{
		return null;
	}

	@Override
	public Point getBase()
	{
		return this.base;
	}

	@Override
	public Vector getDirection()
	{
		return this.direction;
	}

	@Override
	public Point getFurthestXZ()
	{
		return PointUtils.add(this.base, this.direction.clone().multiply(PlotConfig.SIDE_LENGTH));
	}

	@Override
	public Point getFurthestX()
	{
		return PointUtils
				.add(this.base, this.direction.clone().multiply(PlotConfig.SIDE_LENGTH).setZ(0));
	}

	@Override
	public Point getFurthestZ()
	{
		return PointUtils
				.add(this.base, this.direction.clone().multiply(PlotConfig.SIDE_LENGTH).setX(0));
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
		Plot plot = (Plot) o;
		return java.util.Objects.equals(this.base, plot.getBase()) &&
				java.util.Objects.equals(this.direction, plot.getDirection());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.base, this.direction);
	}

}
