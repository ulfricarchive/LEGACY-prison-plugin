package com.ulfric.spigot.prison.warp;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Alias;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.service.ServiceUtils;
import com.ulfric.commons.spigot.text.Text;

@Name("delete")
@Alias("remove")
@Permission("warp-delete")
@MustBePlayer
public class WarpDeleteCommand extends WarpCommand {

    @Override
    public void run(Context context)
    {
        WarpService service = ServiceUtils.getService(WarpService.class);

        if (service.isWarp(this.name))
        {
            service.removeWarp(this.name);

            Text.getService().sendMessage(context.getSender(), "warp-delete");
        }
        else
        {
            Text.getService().sendMessage(context.getSender(), "warp-invalid");
        }

    }

}
