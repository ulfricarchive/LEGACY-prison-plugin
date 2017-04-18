package com.ulfric.spigot.prison.util.serializer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationSerializer extends Serializer<Location> {

    @Override
    public Location from(String context)
    {
        String[] sections = context.split("#");

        if(sections.length > 5)
        {
            World world = Bukkit.getWorld(sections[0]);

            if(world == null)
            {
                return null;
            }

            double x, y, z;

            x = Double.valueOf(sections[1]);
            y = Double.valueOf(sections[2]);
            z = Double.valueOf(sections[3]);

            float yaw, pitch;

            yaw = Float.valueOf(sections[4]);
            pitch = Float.valueOf(sections[5]);

            return new Location(world, x, y, z, yaw, pitch);
        }

        return null;
    }

    @Override
    public String to(Location object)
    {
        StringBuilder builder = new StringBuilder();

        builder.append(object.getWorld().getName());
        builder.append("#");
        builder.append(object.getX());
        builder.append("#");
        builder.append(object.getY());
        builder.append("#");
        builder.append(object.getZ());
        builder.append("#");
        builder.append(object.getYaw());
        builder.append("#");
        builder.append(object.getPitch());

        return builder.toString();
    }

}
