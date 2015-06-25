package net.samagames.hub.cosmetics.gadgets.displayers;

import net.samagames.hub.Hub;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
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
        Bukkit.broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + "Nuke" + ChatColor.DARK_RED + "] " + ChatColor.RED + "Non ! " + player.getName() + " a lancé une Nuke sur le monde ! Tous aux abris !");

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
                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + "Nuke" + ChatColor.DARK_RED + "] " + ChatColor.RED + this.timer);

                for (Player player : players)
                    player.playSound(player.getLocation(), Sound.CAT_MEOW, 1.0F, 1.0F);
            }
        }, 20L, 20L);
    }

    public void timeToSendCatInTheHairLikeTheHandsInTheFamousSing()
    {
        this.loopSecond = Bukkit.getScheduler().runTaskTimerAsynchronously(Hub.getInstance(), new Runnable()
        {
            int loops = 0;
            ArrayList<Ocelot> cats = new ArrayList<>();

            @Override
            public void run()
            {
                loops++;

                if(loops == 300)
                {
                    end();
                    callback();
                }

                Ocelot ocelot = player.getWorld().spawn(player.getLocation().add(0.0D, 1.0D, 0.0D), Ocelot.class);
                ocelot.
                ocelot.setCatType(Ocelot.Type.values()[new Random().nextInt(Ocelot.Type.values().length)]);
                ocelot.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "Meow");
                ocelot.setCustomNameVisible(true);
            }
        }, 2L, 2L);
    }

    public boolean canUse()
    {
        return true;
    }

    private void callback()
    {
        this.loopFirst.cancel();
        this.loopSecond.cancel();
    }
}
