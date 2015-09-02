package net.samagames.hub.events.player;

import net.samagames.hub.Hub;
import net.samagames.hub.parkour.Parkour;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ParkourListener implements Listener
{
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.getAction().equals(Action.PHYSICAL))
        {
            if (event.getClickedBlock().getType().equals(Material.IRON_PLATE))
            {
                Parkour parkour = Hub.getInstance().getParkourManager().getOfPlayer(event.getPlayer().getUniqueId());

                if (parkour != null)
                {
                    Location beginFormatted = new Location(parkour.getBegin().getWorld(), parkour.getBegin().getBlockX(), parkour.getBegin().getBlockY(), parkour.getBegin().getBlockZ());
                    Location endFormatted = new Location(parkour.getEnd().getWorld(), parkour.getEnd().getBlockX(), parkour.getEnd().getBlockY(), parkour.getEnd().getBlockZ());
                    Location blockFormatted = new Location(event.getClickedBlock().getWorld(), event.getClickedBlock().getLocation().getBlockX(), event.getClickedBlock().getLocation().getBlockY(), event.getClickedBlock().getLocation().getBlockZ());


                    if (endFormatted.equals(blockFormatted))
                    {
                        parkour.winPlayer(event.getPlayer());
                        return;
                    }
                    else if (beginFormatted.equals(blockFormatted))
                    {
                        return;
                    }
                    else
                    {
                        parkour.removePlayer(event.getPlayer().getUniqueId());
                    }
                }

                for (Parkour parkourp : Hub.getInstance().getParkourManager().getParkours())
                {
                    Location beginFormatted = new Location(parkourp.getBegin().getWorld(), parkourp.getBegin().getBlockX(), parkourp.getBegin().getBlockY(), parkourp.getBegin().getBlockZ());
                    Location blockFormatted = new Location(event.getClickedBlock().getWorld(), event.getClickedBlock().getLocation().getBlockX(), event.getClickedBlock().getLocation().getBlockY(), event.getClickedBlock().getLocation().getBlockZ());

                    if (beginFormatted.equals(blockFormatted))
                    {
                        parkourp.addPlayer(event.getPlayer());
                        return;
                    }
                }
            }
            else if(event.getClickedBlock().getType().equals(Material.GOLD_PLATE))
            {
                Parkour parkour = Hub.getInstance().getParkourManager().getOfPlayer(event.getPlayer().getUniqueId());

                if (parkour != null)
                {
                    parkour.checkpoint(event.getPlayer(), event.getClickedBlock().getLocation());
                }
            }
        }
    }

    @EventHandler
    public void onLogout(final PlayerQuitEvent event)
    {
        Bukkit.getScheduler().runTaskAsynchronously(Hub.getInstance(), () ->
        {
            Parkour parkour = Hub.getInstance().getParkourManager().getOfPlayer(event.getPlayer().getUniqueId());

            if (parkour != null)
                parkour.removePlayer(event.getPlayer().getUniqueId());
        });
    }

    @EventHandler
    public void onFall(final EntityDamageEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            Player player = (Player) event.getEntity();

            if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL) || event.getCause() == EntityDamageEvent.DamageCause.VOID)
            {
                Location loc = player.getLocation();
                Block block = loc.getBlock();
                loc.setY(loc.getY() - 1);

                Parkour parkour = Hub.getInstance().getParkourManager().getOfPlayer(player.getUniqueId());

                if (parkour == null)
                    return;

                if (block == null || !parkour.inWhitelist(block.getType()))
                {
                    parkour.failPlayer(player);
                }
            }
        }
    }
}
