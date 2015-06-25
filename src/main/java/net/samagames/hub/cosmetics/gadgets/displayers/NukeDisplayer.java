package net.samagames.hub.cosmetics.gadgets.displayers;

import net.minecraft.server.v1_8_R2.EntityOcelot;
import net.samagames.hub.Hub;
import net.samagames.hub.utils.FireworkUtils;
import net.samagames.tools.ColorUtils;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.Random;

public class NukeDisplayer extends AbstractDisplayer
{
    private BukkitTask loopFirst;
    private BukkitTask loopSecond;

    public NukeDisplayer(Player player)
    {
        super(player);
    }

    @Override
    public void display()
    {
        final Collection<Player> players = (Collection<Player>) Bukkit.getOnlinePlayers();
        Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[" + ChatColor.RED + ChatColor.BOLD + "Meow" + ChatColor.DARK_RED + ChatColor.BOLD + "] " + ChatColor.RED + ChatColor.BOLD + "Non ! " + player.getName() + " a lancé une bombe atomique à chat sur le monde ! Tous aux abris !");

        this.loopFirst = Bukkit.getScheduler().runTaskTimerAsynchronously(Hub.getInstance(), new Runnable()
        {
            int timer = 10;

            @Override
            public void run()
            {
                this.timer--;

                if (timer == 0)
                    timeToSendCatInTheHairLikeTheHandsInTheFamousSing();
                else if (timer <= 5)
                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[" + ChatColor.RED + ChatColor.BOLD + "Meow" + ChatColor.DARK_RED + ChatColor.BOLD + "] " + ChatColor.RED + ChatColor.BOLD + this.timer);

                for (Player player : players)
                    player.playSound(player.getLocation(), Sound.CAT_MEOW, 1.0F, 1.0F);
            }
        }, 20L, 20L);
    }

    public void timeToSendCatInTheHairLikeTheHandsInTheFamousSing()
    {
        this.loopFirst.cancel();

        this.loopSecond = Bukkit.getScheduler().runTaskTimer(Hub.getInstance(), new Runnable() {
            int loops = 0;

            @Override
            public void run() {
                loops++;

                if (loops == 120) {
                    end();
                    callback();
                }

                Location toLoc = player.getLocation().add(new Random().nextInt(8), 10, new Random().nextInt(8));
                Vector originVector = player.getLocation().toVector();
                Vector toVector = toLoc.setDirection(toLoc.toVector().subtract(originVector)).toVector();

                final net.minecraft.server.v1_8_R2.World w = ((CraftWorld) player.getWorld()).getHandle();

                EntityOcelot ocelot = new EntityOcelot(w);
                ocelot.setPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
                ocelot.getBukkitEntity().setVelocity(toVector);
                ocelot.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "Meow");
                ocelot.setCustomNameVisible(true);

                w.addEntity(ocelot, CreatureSpawnEvent.SpawnReason.CUSTOM);

                Bukkit.broadcastMessage("spawned");

                for (Player player : Bukkit.getOnlinePlayers())
                    player.playSound(player.getLocation(), Sound.CAT_MEOW, 1.0F, 1.0F);

                Bukkit.getScheduler().runTaskLater(Hub.getInstance(), () ->
                {
                    Color a = ColorUtils.getColor(new Random().nextInt(17) + 1);
                    Color b = ColorUtils.getColor(new Random().nextInt(17) + 1);

                    FireworkEffect fw = FireworkEffect.builder().flicker(true).trail(true).with(FireworkEffect.Type.STAR).withColor(a).withFade(b).build();
                    FireworkUtils.launchfw(ocelot.getBukkitEntity().getLocation(), fw);

                    ocelot.setHealth(0);
                    ocelot.getBukkitEntity().remove();
                }, 20L * 10);
            }
        }, 2L, 2L);
    }

    public boolean canUse()
    {
        return true;
    }

    private void callback()
    {
        this.loopSecond.cancel();
    }
}
