package com.ulfric.spigot.prison.command;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Alias;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.MustBePlayer;
import com.ulfric.commons.spigot.command.Permission;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.text.Text;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Name("nightvision")
@Alias("nv")
@Permission("nightvision-use")
@MustBePlayer
public class CommandNightVision implements Command
{

    @Override
    public void run(Context context)
    {
        Player player = (Player) context.getSender();

        if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION))
        {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);

            Text.getService().sendMessage(player, "nightvision-use-off");
        }
        else
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));

            Text.getService().sendMessage(player, "nightvision-use-on");
        }

    }

}