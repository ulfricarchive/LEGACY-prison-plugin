package com.ulfric.spigot.prison.plots.impl;


import com.ulfric.commons.spigot.guard.Region;
import com.ulfric.commons.spigot.point.PointUtils;
import com.ulfric.commons.spigot.shape.InclusiveCuboid;
import com.ulfric.commons.spigot.shape.Point;
import com.ulfric.spigot.prison.plots.Plot;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class PlayerPlot implements Plot {

	private final Point base;
	private final Vector direction;
	private final UUID uuid;
	private final UUID owner;
	private final Region region;

	public PlayerPlot(UUID owner, Point base, Vector direction)
	{
		this.base = base;
		this.direction = direction;
		this.uuid = UUID.randomUUID();
		this.owner = owner;
		Plot regionPlot = findRegionPlot();
		if (regionPlot != null)
		{
			InclusiveCuboid inclusiveCuboid = new InclusiveCuboid(regionPlot.getBase(),
					Point.builder().setX(regionPlot.getFurthestXZ().getX()).setY(256)
							.setZ(regionPlot.getFurthestXZ().getZ()).build());
			World world = Bukkit.getWorld(PlotConfig.worldName);
			this.region = Region.builder().setBounds(inclusiveCuboid).setName(this.uuid.toString())
					.setWorld(world.getUID()).build();
		}
		else
		{
			this.region = null;
		}
	}

	@Override
	public Region getRegion()
	{
		return this.region;
	}

	@Override
	public UUID getUuid()
	{
		return this.uuid;
	}

	public UUID getOwner()
	{
		return this.owner;
	}

	private Plot findRegionPlot()
	{
		Plot plotX = new TestPlot(this.getFurthestX(),
				this.getDirection().clone().multiply(new Vector(-1, 0, 1)));
		Plot plotZ = new TestPlot(this.getFurthestZ(),
				this.getDirection().clone().multiply(new Vector(1, 0, -1)));
		Plot plotXZ = new TestPlot(this.getFurthestXZ(),
				this.getDirection().clone().multiply(-1));
		Plot regionPlot = null;
		if (plotX.getDirection().equals(new Vector(1, 0, 1)))
		{
			regionPlot = plotX;
		}
		if (plotZ.getDirection().equals(new Vector(1, 0, 1)))
		{
			regionPlot = plotZ;
		}
		if (plotXZ.getDirection().equals(new Vector(1, 0, 1)))
		{
			regionPlot = plotXZ;
		}
		return regionPlot;
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
