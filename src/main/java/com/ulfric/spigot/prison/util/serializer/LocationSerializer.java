package com.ulfric.spigot.prison.util.serializer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.regex.Pattern;

public class LocationSerializer {

    private static final Pattern SEPARATOR = Pattern.compile("#");

    public Location from(String context)
    {
        String[] sections = context.split(LocationSerializer.SEPARATOR.pattern());

        if (sections.length > 5)
        {
            World world = Bukkit.getWorld(sections[0]);

            if (world == null)
            {
                return null;
            }

            double x = Double.valueOf(sections[1]);
            double y = Double.valueOf(sections[2]);
            double z = Double.valueOf(sections[3]);

            float yaw = Float.valueOf(sections[4]);
            float pitch = Float.valueOf(sections[5]);

            return new Location(world, x, y, z, yaw, pitch);
        }

        return null;
    }

    public String to(Location location)
    {
        StringBuilder builder = new StringBuilder(location.getWorld().getName());

        builder.append(LocationSerializer.SEPARATOR.pattern());
        builder.append(location.getX());
        builder.append(LocationSerializer.SEPARATOR.pattern());
        builder.append(location.getY());
        builder.append(LocationSerializer.SEPARATOR.pattern());
        builder.append(location.getZ());
        builder.append(LocationSerializer.SEPARATOR.pattern());
        builder.append(location.getYaw());
        builder.append(LocationSerializer.SEPARATOR.pattern());
        builder.append(location.getPitch());

        return builder.toString();
    }

}
