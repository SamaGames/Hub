package net.samagames.hub.utils;

import net.samagames.hub.Hub;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class ProximityUtils
{
    public static List<Entity> getNearbyEntities(Location center, double radius)
    {
        return getNearbyEntities(center, radius, null);
    }

    public static List<Entity> getNearbyEntities(Location center, double radius, EntityType filter)
    {
        double chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        List<Entity> entities = new ArrayList<>();

        for (double chX = 0 - chunkRadius; chX <= chunkRadius; chX++)
        {
            for (double chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++)
            {
                double x = center.getX();
                double y = center.getY();
                double z = center.getZ();

                for (Entity entity : new Location(center.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities())
                {
                    if (filter != null)
                    {
                        if(entity.getType() != filter)
                            continue;
                    }

                    if (entity.getLocation().distance(center) <= radius && entity.getLocation().getBlock() != center.getBlock())
                        entities.add(entity);
                }
            }
        }

        return entities;
    }

    public static <ENTITY extends Entity> BukkitTask onNearbyOf(Hub hub, Entity entity, double offsetX, double offsetY, double offsetZ, Class<ENTITY> filter, ProximityCallback<ENTITY> callback)
    {
        return hub.getServer().getScheduler().runTaskTimerAsynchronously(hub, () ->
        {
            entity.getNearbyEntities(offsetX, offsetY, offsetZ).stream().forEach(found ->
            {
                if (filter == null || filter.isAssignableFrom(found.getClass()))
                    hub.getServer().getScheduler().runTaskAsynchronously(hub, () -> callback.run((ENTITY) found));
            });
        }, 2L, 2L);
    }

    @FunctionalInterface
    public interface ProximityCallback<ENTITY extends Entity>
    {
        void run(ENTITY entity);
    }
}
