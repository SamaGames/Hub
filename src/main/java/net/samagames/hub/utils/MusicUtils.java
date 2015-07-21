package net.samagames.hub.utils;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

public class MusicUtils
{
    public static Material randomMusic()
    {
        ArrayList<Material> records = new ArrayList<>();

       for(Material material : Material.values())
           if(material.isRecord())
               records.add(material);

        return records.get(new Random().nextInt(records.size()));
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
}
