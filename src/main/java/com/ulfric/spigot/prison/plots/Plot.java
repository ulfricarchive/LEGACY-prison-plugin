package com.ulfric.spigot.prison.plots;


import com.ulfric.commons.bean.Bean;
import com.ulfric.commons.spigot.guard.Region;
import com.ulfric.commons.spigot.point.PointUtils;
import com.ulfric.commons.spigot.shape.InclusiveCuboid;
import com.ulfric.commons.spigot.shape.Point;
import java.util.UUID;
import org.bukkit.util.Vector;

public class Plot extends Bean {

	static final Vector[] DIRECTIONS = new Vector[]{new Vector(1, 0, 1), new Vector(-1, 0, 1),
			new Vector(1, 0, -1), new Vector(-1, 0, -1)};
	private static final int SIDE_LENGTH = 30;

	private final Point base;
	private final Vector direction;
	private final UUID uuid;
	private final UUID owner;
	private Region region;

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
			region = Region.builder().setBounds(inclusiveCuboid).setName(uuid.toString()).build();
		}
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
		Plot plotX = new Plot(owner, this.getFurthestX(),
				this.getDirection().clone().multiply(new Vector(-1, 0, 1)));
		Plot plotZ = new Plot(owner, this.getFurthestZ(),
				this.getDirection().clone().multiply(new Vector(1, 0, -1)));
		Plot plotXZ = new Plot(owner, this.getFurthestXZ(),
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
		return PointUtils.add(base, direction.clone().multiply(SIDE_LENGTH));
	}

	Point getFurthestX()
	{
		return PointUtils.add(base, direction.clone().multiply(SIDE_LENGTH).setZ(0));
	}

	Point getFurthestZ()
	{
		return PointUtils.add(base, direction.clone().multiply(SIDE_LENGTH).setX(0));
	}

}
