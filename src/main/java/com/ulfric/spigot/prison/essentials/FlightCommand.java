package com.ulfric.spigot.prison.essentials;

import org.bukkit.entity.Player;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Alias;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.text.Text;

@Name("flight")
@Alias({"float", "fly", "hover"})
@Permission("flight-use")
@MustBePlayer
class FlightCommand implements Command {

    @Override
    public void run(Context context)
    {
        Player player = (Player) context.getSender();

        boolean status = !player.getAllowFlight();

        player.setAllowFlight(status);
        player.setFlying(status);

        Text.getService().sendMessage(player, status ? "flight-enabled" : "flight-disabled");
    }

}