package net.samagames.hub.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LocationUtils
{
    public static Location str2loc(String loc)
    {
        if (loc == null)
            return null;

        String[] location = loc.split(", ");

        return new Location(Bukkit.getServer().getWorld(location[0]), Double.parseDouble(location[1]), Double.parseDouble(location[2]), Double.parseDouble(location[3]), Float.parseFloat(location[4]), Float.parseFloat(location[5]));
    }

    public static String loc2str(Location loc)
    {
        return loc.getWorld().getName() + ", " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ", " + loc.getYaw() + ", " + loc.getPitch();
    }
}
