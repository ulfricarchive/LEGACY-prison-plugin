package com.ulfric.spigot.prison.spawn;

import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.spigot.prison.warp.Warps;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public final class SpawnService implements Spawn {

    private static final String DEFAULT_SPAWN_NAME = "spawn";

    private Location spawn;

    @Initialize
    public void setup()
    {
        this.updateSpawn();
    }

    private void updateSpawn()
    {
        this.spawn = Warps.getService().getWarp(SpawnService.DEFAULT_SPAWN_NAME);
    }

    @Override
    public void setSpawn(Location spawn)
    {
        this.spawn = spawn;

        Warps.getService().updateWarp(SpawnService.DEFAULT_SPAWN_NAME, spawn);
    }

    @Override
    public Location getSpawn()
    {
        this.updateSpawn();

        return this.spawn;
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
