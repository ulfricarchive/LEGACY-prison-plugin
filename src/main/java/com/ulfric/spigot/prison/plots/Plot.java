package com.ulfric.spigot.prison.plots;

import com.ulfric.commons.spigot.guard.Region;
import com.ulfric.commons.spigot.shape.Point;
import java.util.UUID;
import org.bukkit.util.Vector;

public interface Plot {

	Region getRegion();

	UUID getUuid();

	Point getBase();

	Vector getDirection();

	Point getFurthestXZ();

	Point getFurthestX();

	Point getFurthestZ();
}
