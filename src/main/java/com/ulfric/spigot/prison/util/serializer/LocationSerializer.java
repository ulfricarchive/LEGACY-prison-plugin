package com.ulfric.spigot.prison.util.serializer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.StringJoiner;
import java.util.regex.Pattern;

public class LocationSerializer {

    private static final Pattern SEPARATOR = Pattern.compile("#");

    public Location from(String context)
    {
        String[] sections = LocationSerializer.SEPARATOR.split(context);

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
        StringJoiner joiner = new StringJoiner(LocationSerializer.SEPARATOR.pattern());

        joiner.add(location.getWorld().getName());
        joiner.add(String.valueOf(location.getX()));
        joiner.add(String.valueOf(location.getY()));
        joiner.add(String.valueOf(location.getZ()));
        joiner.add(String.valueOf(location.getYaw()));
        joiner.add(String.valueOf(location.getPitch()));

        return joiner.toString();
    }

}
