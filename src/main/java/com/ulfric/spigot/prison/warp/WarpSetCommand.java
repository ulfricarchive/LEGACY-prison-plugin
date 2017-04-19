package com.ulfric.spigot.prison.warp;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Alias;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.service.ServiceUtils;
import com.ulfric.commons.spigot.text.Text;
import org.bukkit.entity.Player;

@Name("set")
@Alias({"add", "create"})
@Permission("warp-set")
@MustBePlayer
public class WarpSetCommand extends WarpCommand {

    @Override
    public void run(Context context)
    {
        Player player = (Player) context.getSender();

        WarpService service = ServiceUtils.getService(WarpService.class);

        if (service.isWarp(this.name))
        {
            Text.getService().sendMessage(player, "warp-set-already");
        }
        else
        {
            service.setWarp(this.name, player.getLocation());

            Text.getService().sendMessage(player, "warp-set");
        }

    }

}
