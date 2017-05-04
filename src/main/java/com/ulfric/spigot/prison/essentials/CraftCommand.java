package com.ulfric.spigot.prison.essentials;

import org.bukkit.entity.Player;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;

@Name("craft")
@Permission("craft-use")
@MustBePlayer
class CraftCommand implements Command {

    @Override
    public void run(Context context)
    {
        Player player = (Player) context.getSender();
        player.openWorkbench(null, true);
    }

}