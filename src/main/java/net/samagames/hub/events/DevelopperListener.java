package net.samagames.hub.events;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

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
public class DevelopperListener implements Listener
{
    private final Hub hub;

    public DevelopperListener(Hub hub)
    {
        this.hub = hub;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        this.hub.getServer().getScheduler().runTaskAsynchronously(this.hub, () ->
        {
            if (SamaGamesAPI.get().getPermissionsManager().hasPermission(event.getPlayer(), "hub.sign.selection"))
            {
                if (event.getItem() != null && event.getItem().getType() == Material.WOOD_AXE)
                {
                    Action act = event.getAction();

                    if (act == Action.LEFT_CLICK_BLOCK)
                    {
                        this.hub.getPlayerManager().setSelection(event.getPlayer(), event.getClickedBlock().getLocation());
                        event.getPlayer().sendMessage(ChatColor.GOLD + "Point sélectionné !");
                    }
                }
            }
        });
    }
}
