package com.ulfric.spigot.prison.warp;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.service.Service;
import com.ulfric.commons.spigot.service.ServiceUtils;
import com.ulfric.commons.version.Version;
import org.bukkit.Location;

import java.util.List;

@Name("warps")
@Version(1)
public interface Warps extends Service {

    static Warps getService()
    {
        return ServiceUtils.getService(Warps.class);
    }

    void setWarp(String name, Location location);

    void removeWarp(String name);

    Warp getWarp(String name);

    boolean isWarp(String name);

    List<Warp> getWarps();

}
