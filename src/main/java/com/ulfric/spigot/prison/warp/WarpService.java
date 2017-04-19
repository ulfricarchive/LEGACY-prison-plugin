package com.ulfric.spigot.prison.warp;

import com.ulfric.commons.spigot.data.Data;
import com.ulfric.commons.spigot.data.DataStore;
import com.ulfric.commons.spigot.data.PersistentData;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;
import com.ulfric.spigot.prison.util.serializer.LocationSerializer;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.regex.Pattern;

public final class WarpService implements Warps {

    public static DataStore getWarpData(Container container)
    {
        return Data.getDataStore(container).getDataStore("warps");
    }

    private static final Pattern WARP_STORE_SEPARATOR = Pattern.compile("|");

    @Inject
    private Container container;

    private DataStore folder;
    private final List<Warp> warps = new ArrayList<>();
    private final List<String> names = new ArrayList<>();

    @Initialize
    private void initialize()
    {
        this.folder = WarpService.getWarpData(this.container);

        this.load();

        this.updateNames();
    }

    private void load()
    {
        this.warps.clear();

        PersistentData data = this.folder.getData("warps");

        data.getStringList("warps").forEach(content ->
        {
            String[] sections = content.split(WarpService.WARP_STORE_SEPARATOR.pattern());

            if (sections.length > 1)
            {
                Location location = new LocationSerializer().from(sections[1]);

                if (location != null)
                {
                    this.warps.add(Warp.build().setName(sections[0]).setLocation(location).build());
                }

            }

        });

    }

    private void save()
    {
        PersistentData data = this.folder.getData("warps");

        List<String> warps = new ArrayList<>();

        this.warps.forEach(warp -> warps.add(warp.getName() + WarpService.WARP_STORE_SEPARATOR.pattern() + new LocationSerializer().to(warp.getLocation())));

        data.set("warps", warps);
    }

    @Override
    public void setWarp(String name, Location location)
    {
        if (location == null)
        {
            throw new WarpException("Location is null when updating warp, " + name);
        }

        if (isWarp(name))
        {
            getWarp(name).setLocation(location);
        }
        else
        {
            this.warps.add(Warp.build().setName(name).setLocation(location).build());
        }

        this.save();
        this.updateNames();
    }

    @Override
    public void removeWarp(String name)
    {
        if (!isWarp(name))
        {
            throw new WarpException("Invalid warp, " + name + " when attempting to remove.");
        }

        Warp warp = getWarp(name);

        this.warps.remove(warp);

        this.save();
        this.updateNames();
    }

    @Override
    public Warp getWarp(String name)
    {
        return this.warps.stream().filter(warp -> warp.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public boolean isWarp(String name)
    {
        return this.warps.stream().anyMatch(warp -> warp.getName().equalsIgnoreCase(name));
    }

    @Override
    public List<Warp> getWarps()
    {
        return Collections.unmodifiableList(this.warps);
    }

    private void updateNames()
    {
        this.names.clear();

        this.warps.forEach(warp -> this.names.add(warp.getName()));

        Collections.sort(this.names);
    }

    public List<String> getNames()
    {
        return Collections.unmodifiableList(this.names);
    }

}
