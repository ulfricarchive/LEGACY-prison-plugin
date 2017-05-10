package com.ulfric.spigot.prison.plots;

import com.ulfric.commons.service.Service;
import com.ulfric.commons.spigot.shape.Point;
import java.util.List;
import java.util.UUID;
import org.bukkit.util.Vector;

public interface Plots extends Service {

	boolean checkBaseDir(Plot plot, Plot plot1);

	boolean checkCombinations(Plot plot);

	Plot getPlotByBaseDir(Point base, Vector direction);

	Plot getPlotByUUID(UUID uuid);

	Plot getPlotByOwner(UUID owner);

	List<Point> sortPlotsByRadius(Point center, List<Plot> plots);

	Plot generatePlot(UUID owner);

}
