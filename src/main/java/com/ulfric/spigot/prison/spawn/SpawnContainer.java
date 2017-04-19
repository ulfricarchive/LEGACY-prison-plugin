package com.ulfric.spigot.prison.spawn;

import com.ulfric.commons.spigot.service.ServiceUtils;
import com.ulfric.dragoon.ObjectFactory;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;

public class SpawnContainer extends Container {

    @Inject
    private ObjectFactory factory;

    private SpawnService spawn;

    @Initialize
    public void setup()
    {
        this.factory.bind(Spawn.class).to(SpawnService.class);

        this.install(SpawnCommand.class);
        this.install(SetSpawnCommand.class);
    }

    @Override
    public void onEnable()
    {
        this.spawn = ServiceUtils.getService(SpawnService.class);
    }

    @Override
    public void onDisable()
    {
        ServiceUtils.unregister(SpawnService.class, this.spawn);
    }
}
