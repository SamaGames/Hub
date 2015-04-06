package net.samagames.hub.utils;

import net.minecraft.server.v1_8_R2.EntityFireworks;
import net.minecraft.server.v1_8_R2.World;
import net.samagames.hub.Hub;
import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftFirework;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkUtils
{
    public static void launchfw(final Location loc, final FireworkEffect effect)
    {
        final Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        fwm.addEffect(effect);
        fwm.setPower(0);
        fw.setFireworkMeta(fwm);
        ((CraftFirework) fw).getHandle().setInvisible(true);

        Bukkit.getScheduler().runTaskLater(Hub.getInstance(), () ->
        {
            World w = (((CraftWorld) loc.getWorld()).getHandle());
            EntityFireworks fireworks = ((CraftFirework) fw).getHandle();
            w.broadcastEntityEffect(fireworks, (byte) 17);
            fireworks.die();
        }, 1);
    }
}
