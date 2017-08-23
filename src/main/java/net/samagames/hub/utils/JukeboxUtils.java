package net.samagames.hub.utils;

import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

/*
 * This file is part of Hub.
 *
 * Hub is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hub is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hub.  If not, see <http://www.gnu.org/licenses/>.
 */
public class JukeboxUtils
{
    private static final Random RANDOM;

    public static Material randomMusic()
    {
        ArrayList<Material> records = new ArrayList<>();

        for (Material material : Material.values())
            if (material.isRecord())
                records.add(material);

        return records.get(RANDOM.nextInt(records.size()));
    }

    public static void playRecord(Location loc, Material record)
    {
        for (Player p : Bukkit.getOnlinePlayers())
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldEvent(1010, new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), record.getId(), false));
    }

    public static void stopRecord(Location loc)
    {
        for (Player p : Bukkit.getOnlinePlayers())
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldEvent(1010, new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), 0, false));
    }

    static
    {
        RANDOM = new Random();
    }
}
