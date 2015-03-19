package net.samagames.hub.events.protection;

import net.samagames.hub.Hub;
import net.samagames.hub.utils.Selection;
import net.samagames.permissionsbukkit.PermissionsBukkit;
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
        if (!PermissionsBukkit.hasPermission(event.getPlayer(), "lobby.drop"))
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
            if (PermissionsBukkit.hasPermission(event.getPlayer(), "lobby.selection"))
            {
                if (event.getItem() != null && event.getItem().getType() == Material.WOOD_AXE)
                {
                    Action act = event.getAction();
                    Selection selection = Hub.getInstance().getPlayerManager().getSelection(event.getPlayer());

                    if (act == Action.LEFT_CLICK_BLOCK)
                    {
                        if (selection == null)
                            selection = new Selection();

                        selection.setFirstPoint(event.getClickedBlock().getLocation());
                        event.getPlayer().sendMessage(ChatColor.GOLD + "Premier point établi.");
                    }
                    else if (act == Action.RIGHT_CLICK_BLOCK)
                    {
                        if (selection == null)
                            selection = new Selection();

                        selection.setLastPoint(event.getClickedBlock().getLocation());
                        event.getPlayer().sendMessage(ChatColor.GOLD + "Second point établi.");
                    }

                    Hub.getInstance().getPlayerManager().setSelection(event.getPlayer(), selection);
                }
            }
        });
    }
}
