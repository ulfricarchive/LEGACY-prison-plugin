package com.ulfric.spigot.prison.warp;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.command.argument.Argument;
import com.ulfric.commons.spigot.text.Text;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Name("warp")
@Permission("warp-use")
@MustBePlayer
public class WarpCommand implements Command
{

    @Argument
    protected String name;

    @Override
    public void run(Context context)
    {
        Player player = (Player) context.getSender();

        if(Warps.getService().isWarp(this.name))
        {
            Location location = Warps.getService().getWarp(this.name);

            player.teleport(location);

            Text.getService().sendMessage(player, "warp-teleport");
        }
        else
        {
            Text.getService().sendMessage(player, "warp-invalid");
        }

    }

}
