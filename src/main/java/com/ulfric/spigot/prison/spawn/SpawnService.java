package com.ulfric.spigot.prison.spawn;

import com.ulfric.spigot.prison.warp.Warp;
import com.ulfric.spigot.prison.warp.Warps;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public final class SpawnService implements Spawn {

    private static final String DEFAULT_SPAWN_NAME = "spawn";

    @Override
    public void setSpawn(Location spawn)
    {
        Warps warps = Warps.getService();

        if (spawn == null)
        {
            if (warps.isWarp(SpawnService.DEFAULT_SPAWN_NAME))
            {
                warps.removeWarp(SpawnService.DEFAULT_SPAWN_NAME);
            }
        }
        else
        {
            warps.setWarp(SpawnService.DEFAULT_SPAWN_NAME, spawn);
        }

    }

    @Override
    public Location getSpawn()
    {
        Warp warp = Warps.getService().getWarp(SpawnService.DEFAULT_SPAWN_NAME);

        return warp == null ? null : warp.getLocation();
    }

    @Override
    public boolean isSpawnSet()
    {
        return getSpawn() != null;
    }

    @Override
    public void teleport(Entity entity)
    {
        Location spawn = getSpawn();

        if (spawn == null)
        {
            throw new SpawnException("Spawn is not set");
        }

        entity.teleport(spawn);
    }

}
