package net.samagames.hub.events.protection;

import net.samagames.hub.Hub;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class InventoryEditionListener implements Listener
{
    private final Hub hub;

    public InventoryEditionListener(Hub hub)
    {
        this.hub = hub;
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event)
    {
        if (!(event.getWhoClicked() instanceof Player) || !canDoAction((Player) event.getWhoClicked()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if (!(event.getWhoClicked() instanceof Player) || !canDoAction((Player) event.getWhoClicked()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent event)
    {
        if (!(event.getWhoClicked() instanceof Player) || !canDoAction((Player) event.getWhoClicked()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onSecondHandItemSwap(PlayerSwapHandItemsEvent event)
    {
        if (!canDoAction(event.getPlayer()))
            event.setCancelled(true);
    }

    private boolean canDoAction(Player player)
    {
        return this.hub.getPlayerManager().canBuild() && player != null && player.isOp() && player.getGameMode() == GameMode.CREATIVE;
    }
}
