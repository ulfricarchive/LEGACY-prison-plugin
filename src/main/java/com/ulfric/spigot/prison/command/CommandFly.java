package com.ulfric.spigot.prison.command;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.text.Text;
import org.bukkit.entity.Player;

@Name("fly")
@Permission("fly-use")
@MustBePlayer
public class CommandFly implements Command
{

    @Override
    public void run(Context context)
    {
        Player player = (Player) context.getSender();

        boolean status = !player.getAllowFlight();

        player.setAllowFlight(status);
        player.setFlying(status);

        Text.getService().sendMessage(player, status ? "fly-use-on" : "fly-use-off");
    }

}
