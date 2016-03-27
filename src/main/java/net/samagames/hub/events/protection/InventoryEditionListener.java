package net.samagames.hub.events.protection;

import net.samagames.hub.Hub;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;

public class InventoryEditionListener implements Listener
{
    private final Hub hub;

    public InventoryEditionListener(Hub hub)
    {
        this.hub = hub;
    }

    @EventHandler
    public void onCraftItemEvent(CraftItemEvent event)
    {
        if (!(event.getWhoClicked() instanceof Player) || !canDoAction((Player) event.getWhoClicked()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onFurnaceBurnEvent(FurnaceBurnEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFurnaceSmeltEvent(FurnaceSmeltEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event)
    {
        if (!(event.getWhoClicked() instanceof Player) || !canDoAction((Player) event.getWhoClicked()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryInteractEvent(InventoryInteractEvent event)
    {
        if (!(event.getWhoClicked() instanceof Player) || !canDoAction((Player) event.getWhoClicked()))
            event.setCancelled(true);
    }

    private boolean canDoAction(Player player)
    {
        return this.hub.getPlayerManager().canBuild() && player != null && player.isOp() && player.getGameMode() == GameMode.CREATIVE;
    }
}
