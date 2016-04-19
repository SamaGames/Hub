package net.samagames.hub.utils;

import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.PacketPlayOutWorldEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

public class JukeboxUtils
{
    private static final Random RANDOM;

    public static Material randomMusic()
    {
        ArrayList<Material> records = new ArrayList<>();

        for(Material material : Material.values())
            if(material.isRecord())
                records.add(material);

        return records.get(RANDOM.nextInt(records.size()));
    }

    public static void playRecord(Location loc, Material record)
    {
        for(Player p : Bukkit.getOnlinePlayers())
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldEvent(1005, new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), record.getId(), false));
    }

    public static void stopRecord(Location loc)
    {
        for(Player p : Bukkit.getOnlinePlayers())
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldEvent(1005, new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), 0, false));
    }

    static
    {
        RANDOM = new Random();
    }
}
