package net.samagames.hub.events.protection;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;

public class PlayerEditionListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerAchievementAwardedEvent(PlayerAchievementAwardedEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBucketFillEvent(PlayerBucketFillEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDropItemEvent(PlayerDropItemEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event)
    {
        if (Hub.getInstance().getPlayerManager().canBuild())
        {
            if (!event.getPlayer().isOp())
                event.setCancelled(true);
        }
        else
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractEvent(final PlayerInteractEvent event)
    {
        if (Hub.getInstance().getPlayerManager().canBuild())
        {
            if (!event.getPlayer().isOp())
                event.setCancelled(true);
        }
        else
        {
            event.setCancelled(true);
        }

        Bukkit.getScheduler().runTaskAsynchronously(Hub.getInstance(), () ->
        {
            if (SamaGamesAPI.get().getPermissionsManager().hasPermission(event.getPlayer(), "hub.sign.selection"))
            {
                if (event.getItem() != null && event.getItem().getType() == Material.WOOD_AXE)
                {
                    Action act = event.getAction();

                    if (act == Action.LEFT_CLICK_BLOCK)
                    {
                        Hub.getInstance().getPlayerManager().setSelection(event.getPlayer(), event.getClickedBlock().getLocation());
                        event.getPlayer().sendMessage(ChatColor.GOLD + "Point sélectionné !");
                    }
                }
            }
        });
    }
}
