package com.ulfric.spigot.prison.plots;


import com.ulfric.commons.bean.Bean;
import com.ulfric.commons.spigot.point.PointUtils;
import com.ulfric.commons.spigot.shape.Point;
import org.bukkit.util.Vector;

public class Plot extends Bean {

	static final Vector[] DIRECTIONS = new Vector[]{new Vector(1, 0, 1), new Vector(-1, 0, 1),
			new Vector(1, 0, -1), new Vector(-1, 0, -1)};
	private final Point base;
	private final Vector direction;
	private final int sideLength;

	Plot(Point base, Vector direction, int sideLength)
	{
		this.base = base;
		this.direction = direction;
		this.sideLength = sideLength;
	}

	Point getBase()
	{
		return base;
	}

	Vector getDirection()
	{
		return direction;
	}

	int getSideLength()
	{
		return sideLength;
	}

	Point getXZPoint()
	{
		return PointUtils.add(base, direction.clone().multiply(sideLength));
	}

	Point getXPoint()
	{
		return PointUtils.add(base, direction.clone().multiply(sideLength).setZ(0));
	}

	Point getZPoint()
	{
		return PointUtils.add(base, direction.clone().multiply(sideLength).setX(0));
	}

}
