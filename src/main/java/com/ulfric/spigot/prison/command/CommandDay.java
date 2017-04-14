package com.ulfric.spigot.prison.command;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.text.Text;
import org.bukkit.entity.Player;

@Name("day")
@Permission("day-use")
@MustBePlayer
public class CommandDay implements Command
{

    @Override
    public void run(Context context)
    {
        Player player = (Player) context.getSender();

        player.setPlayerTime(10_000L, true);

        Text.getService().sendMessage(player, "day-use");
    }

}
