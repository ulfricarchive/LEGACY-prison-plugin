package com.ulfric.spigot.prison.warp;

import com.ulfric.commons.spigot.data.Data;
import com.ulfric.commons.spigot.data.DataStore;
import com.ulfric.commons.spigot.data.PersistentData;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;
import com.ulfric.spigot.prison.util.serializer.LocationSerializer;
import org.bukkit.Location;

import java.util.*;

public class WarpService implements Warps {

    public static DataStore getWarpData(Container container)
    {
        return Data.getDataStore(container).getDataStore("warps");
    }

    @Inject
    private Container container;

    private DataStore folder;
    private Map<String, Location> warps;

    @Initialize
    private void initialize()
    {
        this.folder = WarpService.getWarpData(this.container);
        this.warps = new HashMap<>();

        this.load();
    }

    private void load()
    {
        this.warps.clear();

        PersistentData data = this.folder.getData("warps");

        data.getStringList("warps").forEach(content ->
        {
            String[] sections = content.split("|");

            if(sections.length > 1)
            {
                Location location = new LocationSerializer().from(sections[1]);

                if(location != null)
                {
                    addWarp(sections[0], location);
                }

            }

        });

    }

    protected void save()
    {
        PersistentData data = this.folder.getData("warps");

        List<String> warp = new ArrayList<>();

        this.warps.forEach((name, location) -> warp.add(name + "|" + new LocationSerializer().to(location)));

        data.set("warps", warp);
    }

    @Override
    public void addWarp(String name, Location location)
    {
        if(this.warps.containsKey(name))
        {
            throw new WarpException("Warp, " + name + " already exists.");
        }

        this.warps.put(name, location);
    }

    @Override
    public void removeWarp(String name)
    {
        if(!this.warps.containsKey(name))
        {
            throw new WarpException("Invalid warp, " + name + " when attempting to remove.");
        }

        this.warps.remove(name);
    }

    @Override
    public Location getWarp(String name)
    {
        return this.warps.getOrDefault(name, null);
    }

    @Override
    public boolean isWarp(String name)
    {
        return this.warps.containsKey(name);
    }

    @Override
    public Map<String, Location> getWarps()
    {
        return Collections.unmodifiableMap(this.warps);
    }

}
