package net.samagames.hub.cosmetics.balloons;

import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.ClickType;

import javax.lang.model.type.NullType;

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
public class BalloonManager extends AbstractCosmeticManager<BalloonCosmetic> implements Listener
{
    public BalloonManager(Hub hub)
    {
        super(hub, new BalloonRegistry(hub));
        this.hub.getServer().getPluginManager().registerEvents(this, this.hub);
    }

    @Override
    public void enableCosmetic(Player player, BalloonCosmetic cosmetic, ClickType clickType, boolean login, NullType useless)
    {
        cosmetic.spawn(player);

        if (!login)
            player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.GREEN + "Des ballons flottent autour de vous !");
    }

    @Override
    public void disableCosmetic(Player player, BalloonCosmetic cosmetic, boolean logout, boolean replace, NullType useless)
    {
        cosmetic.remove(player);

        if (!logout && !replace)
            player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.GREEN + "Vos ballons ont éclatés.");
    }

    @Override
    public void update() { /** Not needed **/ }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event)
    {
        if (event.getEntity().getItemStack() != null && event.getEntity().getItemStack().getType() == Material.LEASH)
            event.setCancelled(true);
    }

    @Override
    public boolean restrictToOne()
    {
        return true;
    }
}
