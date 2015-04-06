package net.samagames.hub.cosmetics.gadgets.displayers;

import net.samagames.hub.Hub;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Collection;

public class NukeDisplayer extends AbstractDisplayer
{
    private int loopId;

    public NukeDisplayer(Player player)
    {
        super(player);
    }

    @Override
    public void display()
    {
        final Collection<Player> players = (Collection<Player>) Bukkit.getOnlinePlayers();
        Bukkit.broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + "Nuke" + ChatColor.DARK_RED + "] " + ChatColor.RED + "Non ! " + player.getName() + " a lancé une Nuke sur le monde ! Tous aux abris !");

        this.loopId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Hub.getInstance(), new Runnable()
        {
            int timer = 10;

            @Override
            public void run()
            {
                this.timer--;

                if (timer == 0)
                {
                    for(Player player : players)
                    {
                        player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 1.0F, false, false);
                    }

                    Bukkit.getScheduler().runTaskLater(Hub.getInstance(), new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            for(Player player : players)
                            {
                                player.teleport(player.getLocation().add(0.0D, 300.0D, 0.0D));
                                player.playSound(player.getLocation(), Sound.WITHER_SPAWN, 1.0F, 1.0F);
                            }
                        }
                    }, 5L);

                    end();
                    callback();
                }
                else if (timer <= 5)
                {
                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + "Nuke" + ChatColor.DARK_RED + "] " + ChatColor.RED + this.timer);
                }

                for(Player player : players)
                {
                    player.playSound(player.getLocation(), Sound.CAT_MEOW, 1.0F, 1.0F);
                }
            }
        }, 20L, 20L);
    }

    public boolean canUse()
    {
        return true;
    }

    private void callback()
    {
        Bukkit.getScheduler().cancelTask(this.loopId);
    }
}
