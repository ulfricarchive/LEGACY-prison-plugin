package com.ulfric.spigot.prison.spawn;

import com.ulfric.commons.spigot.data.Data;
import com.ulfric.commons.spigot.data.DataStore;
import com.ulfric.commons.spigot.data.PersistentData;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;
import com.ulfric.spigot.prison.util.serializer.LocationSerializer;
import org.bukkit.Location;

public final class SpawnService implements Spawn {

    public static DataStore getSpawnData(Container container)
    {
        return Data.getDataStore(container).getDataStore("spawn");
    }

    @Inject
    private Container container;

    private DataStore folder;
    private Location spawn;

    @Initialize
    public void setup()
    {
        this.folder = SpawnService.getSpawnData(this.container);

        this.load();
    }

    private void load()
    {
        PersistentData data = this.folder.getData("spawn");

        String context = data.getString("spawn");

        if (context != null)
        {
            this.spawn = new LocationSerializer().from(context.trim());
        }
    }

    private void save()
    {
        PersistentData data = this.folder.getData("spawn");

        data.set("spawn", isSpawnSet() ? new LocationSerializer().to(this.spawn) : null);

        data.save();
    }

    @Override
    public void setSpawn(Location spawn)
    {
        this.spawn = spawn;

        this.save();
    }

    @Override
    public Location getSpawn()
    {
        return this.spawn;
    }

    @Override
    public boolean isSpawnSet()
    {
        return this.spawn != null;
    }

}
