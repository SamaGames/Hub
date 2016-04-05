package net.samagames.hub.events;

import net.samagames.hub.Hub;
import net.samagames.hub.parkours.Parkour;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ParkourListener implements Listener
{
    private final Hub hub;

    public ParkourListener(Hub hub)
    {
        this.hub = hub;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.getAction().equals(Action.PHYSICAL))
        {
            if(event.getClickedBlock().getType().equals(Material.GOLD_PLATE))
            {
                Parkour parkour = this.hub.getParkourManager().getPlayerParkour(event.getPlayer().getUniqueId());

                if (parkour != null)
                    parkour.checkpoint(event.getPlayer(), event.getClickedBlock().getLocation());
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        this.hub.getServer().getScheduler().runTaskAsynchronously(this.hub, () ->
        {
            Parkour parkour = this.hub.getParkourManager().getPlayerParkour(event.getPlayer().getUniqueId());

            if (parkour != null)
                parkour.removePlayer(event.getPlayer());
        });
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        this.hub.getServer().getScheduler().runTask(this.hub, () ->
        {
            Parkour parkour = this.hub.getParkourManager().getPlayerParkour(event.getPlayer().getUniqueId());

            if (parkour != null && event.getTo().getBlockY() <= parkour.getMinimalHeight())
                parkour.failPlayer(event.getPlayer());
        });
    }
}
