package net.samagames.hub.events.protection;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;

public class InventoryEditionListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCraftItemEvent(CraftItemEvent event)
    {
        if (!event.getWhoClicked().isOp())
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFurnaceBurnEvent(FurnaceBurnEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFurnaceSmeltEvent(FurnaceSmeltEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClickEvent(InventoryClickEvent event)
    {
        if (!event.getWhoClicked().isOp() || event.getWhoClicked().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryInteractEvent(InventoryInteractEvent event)
    {
        if (!event.getWhoClicked().isOp() || event.getWhoClicked().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
    }
}
