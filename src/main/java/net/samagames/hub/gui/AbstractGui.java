package net.samagames.hub.gui;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.utils.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
public abstract class AbstractGui extends net.samagames.api.gui.AbstractGui
{
    protected final Hub hub;

    public AbstractGui(Hub hub)
    {
        this.hub = hub;
    }

    protected static ItemStack getBackIcon()
    {
        ItemStack stack = new ItemStack(Material.EMERALD, 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "« Retour");
        stack.setItemMeta(meta);

        return stack;
    }

    protected static ItemStack getCoinsIcon(Player player)
    {
        long coins = SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).getCoins();

        ItemStack stack = new ItemStack(Material.GOLD_INGOT, 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Vous avez " + NumberUtils.format(coins) + " pièces");
        stack.setItemMeta(meta);

        return stack;
    }

    protected static ItemStack getPowdersIcon(Player player)
    {
        long powders = SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).getPowders();

        ItemStack stack = new ItemStack(Material.SUGAR, 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Vous avez " + NumberUtils.format(powders) + " poussières d'\u272F");
        stack.setItemMeta(meta);

        return stack;
    }

    protected int getSlot(String action)
    {
        for (int slot : this.actions.keySet())
            if (this.actions.get(slot).equals(action))
                return slot;

        return 0;
    }
}
