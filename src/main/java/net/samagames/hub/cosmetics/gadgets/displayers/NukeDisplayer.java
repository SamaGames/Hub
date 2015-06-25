package net.samagames.hub.cosmetics.gadgets.displayers;

import net.samagames.hub.Hub;
import net.samagames.hub.utils.FireworkUtils;
import net.samagames.tools.ColorUtils;
import org.bukkit.*;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
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

        this.loopFirst = Bukkit.getScheduler().runTaskTimerAsynchronously(Hub.getInstance(), new Runnable() {
            int timer = 10;

            @Override
            public void run() {
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

        this.loopSecond = Bukkit.getScheduler().runTaskTimer(Hub.getInstance(), new Runnable()
        {
            int loops = 0;

            @Override
            public void run()
            {
                loops++;

                if (loops == 500)
                {
                    end();
                    callback();
                }
                else if (loops == 1)
                {
                    player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 5.0F, false, false);
                }

                Ocelot ocelot = player.getWorld().spawn(player.getLocation(), Ocelot.class);
                ocelot.setVelocity(new Vector(new Random().nextInt(8) - 4, 3, new Random().nextInt(8) - 4));
                ocelot.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "Meow");
                ocelot.setCustomNameVisible(true);

                for (Player player : Bukkit.getOnlinePlayers())
                    player.playSound(player.getLocation(), Sound.CAT_MEOW, 1.0F, 1.0F);

                Bukkit.getScheduler().runTaskLater(Hub.getInstance(), () ->
                {
                    Color a = ColorUtils.getColor(new Random().nextInt(17) + 1);
                    Color b = ColorUtils.getColor(new Random().nextInt(17) + 1);

                    FireworkEffect fw = FireworkEffect.builder().flicker(true).trail(true).with(FireworkEffect.Type.STAR).withColor(a).withFade(b).build();
                    FireworkUtils.launchfw(ocelot.getLocation(), fw);

                    ocelot.setHealth(0);
                    ocelot.remove();
                }, 20L * 5);
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
