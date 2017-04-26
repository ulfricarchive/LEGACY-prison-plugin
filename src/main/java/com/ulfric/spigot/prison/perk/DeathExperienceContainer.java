package com.ulfric.spigot.prison.perk;

import com.ulfric.commons.spigot.intercept.RequirePermission;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathExperienceContainer extends Container {

    @Initialize
    private void initialize()
    {
        this.install(DeathExperienceListener.class);
    }

    private static final class DeathExperienceListener implements Listener
    {

        @EventHandler
        @RequirePermission(permission = "keep-death-experience")
        public void onDeath(PlayerDeathEvent event)
        {
            event.setDroppedExp(0);
            event.setKeepLevel(true);
        }

    }

}
