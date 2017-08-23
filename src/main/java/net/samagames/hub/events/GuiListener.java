package net.samagames.hub.events;

import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.PlayerInventory;

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
public class GuiListener implements Listener
{
    private final Hub hub;

    public GuiListener(Hub hub)
    {
        this.hub = hub;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if (event.getWhoClicked() instanceof Player)
        {
            Player player = (Player) event.getWhoClicked();
            AbstractGui gui = (AbstractGui) this.hub.getGuiManager().getPlayerGui(player);

            if (event.getClickedInventory() instanceof PlayerInventory)
            {
                this.hub.getPlayerManager().getStaticInventory().doInteraction(player, event.getCurrentItem());
                return;
            }

            if (gui != null)
            {
                String action = gui.getAction(event.getSlot());

                if (action != null)
                    gui.onClick(player, event.getCurrentItem(), action, event.getClick());
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        if (this.hub.getGuiManager().getPlayerGui(event.getPlayer()) != null)
            this.hub.getGuiManager().removeClosedGui((Player) event.getPlayer());
    }
}
