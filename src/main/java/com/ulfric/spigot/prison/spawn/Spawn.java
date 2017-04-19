package com.ulfric.spigot.prison.spawn;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.service.Service;
import com.ulfric.commons.spigot.service.ServiceUtils;
import com.ulfric.commons.version.Version;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

@Name("spawn")
@Version(1)
public interface Spawn extends Service {

    static Spawn getService()
    {
        return ServiceUtils.getService(Spawn.class);
    }

    void setSpawn(Location location);

    Location getSpawn();

    boolean isSpawnSet();

    void teleport(Entity entity);

}
