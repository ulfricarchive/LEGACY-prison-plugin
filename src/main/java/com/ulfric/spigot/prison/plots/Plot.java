package com.ulfric.spigot.prison.plots;


import com.google.gson.annotations.Expose;
import com.ulfric.commons.spigot.guard.Region;
import com.ulfric.commons.spigot.point.PointUtils;
import com.ulfric.commons.spigot.shape.InclusiveCuboid;
import com.ulfric.commons.spigot.shape.Point;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class Plot {

	@Expose
	private final Point base;
	@Expose
	private final Vector direction;
	@Expose
	private final UUID uuid;
	@Expose
	private final UUID owner;
	private final Region region;

	Plot(UUID owner, Point base, Vector direction)
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

	Plot(Point base, Vector direction)
	{
		this.base = base;
		this.direction = direction;
		this.uuid = null;
		this.owner = null;
		this.region = null;
	}

	public Region getRegion()
	{
		return region;
	}

	public UUID getUuid()
	{
		return uuid;
	}

	public UUID getOwner()
	{
		return owner;
	}

	private Plot findRegionPlot()
	{
		Plot plotX = new Plot(this.getFurthestX(),
				this.getDirection().clone().multiply(new Vector(-1, 0, 1)));
		Plot plotZ = new Plot(this.getFurthestZ(),
				this.getDirection().clone().multiply(new Vector(1, 0, -1)));
		Plot plotXZ = new Plot(this.getFurthestXZ(),
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

	Point getBase()
	{
		return base;
	}

	Vector getDirection()
	{
		return direction;
	}

	Point getFurthestXZ()
	{
		return PointUtils.add(base, direction.clone().multiply(PlotConfig.SIDE_LENGTH));
	}

	Point getFurthestX()
	{
		return PointUtils.add(base, direction.clone().multiply(PlotConfig.SIDE_LENGTH).setZ(0));
	}

	Point getFurthestZ()
	{
		return PointUtils.add(base, direction.clone().multiply(PlotConfig.SIDE_LENGTH).setX(0));
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
		return java.util.Objects.equals(base, plot.base) &&
				java.util.Objects.equals(direction, plot.direction);
	}

	@Override
	public int hashCode()
	{
		return java.util.Objects.hash(base, direction);
	}
}
