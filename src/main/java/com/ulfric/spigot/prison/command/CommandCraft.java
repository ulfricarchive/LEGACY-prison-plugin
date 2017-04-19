package com.ulfric.spigot.prison.command;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

@Name("craft")
@Permission("craft-use")
@MustBePlayer
public class CommandCraft implements Command {

    @Override
    public void run(Context context)
    {
        Player player = (Player) context.getSender();

        Inventory inventory = Bukkit.createInventory(player, InventoryType.CRAFTING);

        player.openInventory(inventory);
    }

}