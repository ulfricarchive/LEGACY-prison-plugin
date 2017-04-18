package com.ulfric.spigot.prison.warp;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.text.Text;

@Name("warps")
@Permission("warps-use")
@MustBePlayer
public class WarpsCommand implements Command {

    @Override
    public void run(Context context)
    {
        Text.getService().sendMessage(context.getSender(), "warps");
    }

}
