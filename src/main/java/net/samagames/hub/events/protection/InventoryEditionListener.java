package net.samagames.hub.events.protection;

import net.samagames.hub.Hub;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

/*
 * This file is part of Hub.
 *
 * Hub is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hub is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hub.  If not, see <http://www.gnu.org/licenses/>.
 */
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
        if (!(event.getWhoClicked() instanceof Player) || !this.canDoAction((Player) event.getWhoClicked()))
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
        if (!(event.getWhoClicked() instanceof Player) || !this.canDoAction((Player) event.getWhoClicked()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent event)
    {
        if (!(event.getWhoClicked() instanceof Player) || !this.canDoAction((Player) event.getWhoClicked()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onSecondHandItemSwap(PlayerSwapHandItemsEvent event)
    {
        event.setCancelled(true);
    }

    private boolean canDoAction(Player player)
    {
        return this.hub.getPlayerManager().canBuild() && player != null && player.isOp() && player.getGameMode() == GameMode.CREATIVE;
    }
}
