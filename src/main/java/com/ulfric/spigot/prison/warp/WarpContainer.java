package com.ulfric.spigot.prison.warp;

import com.ulfric.commons.spigot.service.ServiceUtils;
import com.ulfric.dragoon.ObjectFactory;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;

public class WarpContainer extends Container {

    @Inject
    private ObjectFactory factory;

    private WarpService warps;

    @Initialize
    private void setup()
    {
        this.factory.bind(Warps.class).to(WarpService.class);

        this.install(WarpService.class);
        this.install(WarpCommand.class);
        this.install(WarpsCommand.class);
        this.install(WarpSetCommand.class);
        this.install(WarpDeleteCommand.class);
        this.install(WarpsPlaceholder.class);
    }

    @Override
    public void onEnable()
    {
        this.warps = ServiceUtils.getService(WarpService.class);
    }

    @Override
    public void onDisable()
    {
        ServiceUtils.unregister(WarpService.class, this.warps);
    }

}
