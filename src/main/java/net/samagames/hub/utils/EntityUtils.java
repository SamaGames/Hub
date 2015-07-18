package net.samagames.hub.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;

public class EntityUtils
{
    public static ArrayList<Entity> getNearbyEntities(Location center, double radius)
    {
        return getNearbyEntities(center, radius, null);
    }

    public static ArrayList<Entity> getNearbyEntities(Location center, double radius, EntityType target)
    {
        double chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        ArrayList<Entity> entities = new ArrayList<>();

        for (double chX = 0 - chunkRadius; chX <= chunkRadius; chX++)
        {
            for (double chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++)
            {
                double x = center.getX();
                double y = center.getY();
                double z = center.getZ();

                for (Entity entity : new Location(center.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities())
                {
                    if (target != null)
                    {
                        if(entity.getType() != target)
                            continue;
                    }

                    if (entity.getLocation().distance(center) <= radius && entity.getLocation().getBlock() != center.getBlock())
                        entities.add(entity);
                }
            }
        }

        return entities;
    }
}
