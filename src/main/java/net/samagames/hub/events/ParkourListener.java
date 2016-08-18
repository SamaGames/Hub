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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ParkourListener implements Listener
{
    private final Hub hub;
    private List<UUID> cooldown;

    public ParkourListener(Hub hub)
    {
        this.hub = hub;
        this.cooldown = new ArrayList<>();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.getAction().equals(Action.PHYSICAL))
        {
            if (event.getClickedBlock().getType().equals(Material.GOLD_PLATE) || event.getClickedBlock().getType().equals(Material.IRON_PLATE))
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
        if (this.cooldown.contains(event.getPlayer().getUniqueId()))
            return ;
        this.cooldown.add(event.getPlayer().getUniqueId());
        this.hub.getServer().getScheduler().runTask(this.hub, () ->
        {
            this.cooldown.remove(event.getPlayer().getUniqueId());
            Parkour parkour = this.hub.getParkourManager().getPlayerParkour(event.getPlayer().getUniqueId());

            if (parkour != null && event.getTo().getBlockY() <= parkour.getMinimalHeight())
                parkour.failPlayer(event.getPlayer());
        });
    }
}
