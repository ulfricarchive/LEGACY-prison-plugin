package com.ulfric.spigot.prison.warp;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.service.Service;
import com.ulfric.commons.spigot.service.ServiceUtils;
import com.ulfric.commons.version.Version;
import org.bukkit.Location;

import java.util.Map;

@Name("warps")
@Version(1)
public interface Warps extends Service {

    static Warps getService()
    {
        return ServiceUtils.getService(Warps.class);
    }

    void addWarp(String name, Location location);

    void removeWarp(String name);

    Location getWarp(String name);

    boolean isWarp(String name);

    Map<String, Location> getWarps();

}
