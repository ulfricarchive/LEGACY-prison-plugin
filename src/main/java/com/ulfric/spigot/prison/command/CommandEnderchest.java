package com.ulfric.spigot.prison.command;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.*;
import org.bukkit.entity.Player;

@Name("enderchest")
@Alias("ec")
@Permission("enderchest-use")
@MustBePlayer
public class CommandEnderchest implements Command
{

    @Override
    public void run(Context context)
    {
        Player player = (Player) context.getSender();

        player.openInventory(player.getEnderChest());
    }

}
