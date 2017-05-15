package com.ulfric.spigot.prison.plots;

import com.ulfric.commons.spigot.shape.Point;
import java.util.Set;
import java.util.UUID;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public interface Plots extends Listener {

	static PlotsService getService()
	{
		return PlotsService.instance;
	}

	void loadPlots();

	void savePlots();

	Plot generatePlot(UUID owner);

	boolean checkBaseDir(Plot plot, Plot plot1);

	boolean checkCombinations(Plot plot);

	Plot getPlotByBaseDir(Point base, Vector direction);

	Plot getPlotByUUID(UUID uuid);

	Set<Plot> getPlotByOwner(UUID owner);

	Set<Point> sortPlotsByRadius(Point center, Set<Plot> plots);
}
