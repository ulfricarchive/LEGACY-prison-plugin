package com.ulfric.spigot.prison.plots;


import com.ulfric.commons.bean.Bean;
import com.ulfric.commons.spigot.point.PointUtils;
import com.ulfric.commons.spigot.shape.Point;
import org.bukkit.util.Vector;

public class Plot extends Bean {

	public static final Vector[] DIRECTIONS = new Vector[]{new Vector(1, 0, 1), new Vector(-1, 0, 1),
			new Vector(1, 0, -1), new Vector(-1, 0, -1)};
	private final Point base;
	private final Vector direction;
	private final int sideLength;

	public Plot(Point base, Vector direction, int sideLength)
	{
		this.base = base;
		this.direction = direction;
		this.sideLength = sideLength;
	}

	public Point getBase()
	{
		return base;
	}

	public Vector getDirection()
	{
		return direction;
	}

	public int getSideLength()
	{
		return sideLength;
	}

	public Point max()
	{
		return PointUtils.add(base, direction.clone().multiply(sideLength));
	}

}
