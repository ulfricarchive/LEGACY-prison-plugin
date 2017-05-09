package com.ulfric.spigot.prison.plots;


import com.ulfric.commons.bean.Bean;
import com.ulfric.commons.spigot.point.PointUtils;
import com.ulfric.commons.spigot.shape.Point;
import org.bukkit.util.Vector;

public class Plot extends Bean {

	static final Vector[] DIRECTIONS = new Vector[]{new Vector(1, 0, 1), new Vector(-1, 0, 1),
			new Vector(1, 0, -1), new Vector(-1, 0, -1)};
	static final int SIDE_LENGTH = 30;
	private final Point base;
	private final Vector direction;

	Plot(Point base, Vector direction, int sideLength)
	{
		this.base = base;
		this.direction = direction;
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
