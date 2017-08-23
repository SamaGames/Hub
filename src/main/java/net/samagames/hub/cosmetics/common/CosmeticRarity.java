package net.samagames.hub.cosmetics.common;

import org.bukkit.ChatColor;

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
public enum CosmeticRarity
{
    COMMON("Commun", ChatColor.GREEN),
    RARE("Rare", ChatColor.BLUE),
    EPIC("Epic", ChatColor.DARK_PURPLE),
    LEGENDARY("Légendaire", ChatColor.GOLD),
    STAFF("Réservé à l'équipe", ChatColor.RED),
    ADMIN("Réservé aux administrateurs", ChatColor.RED),
    ;

    private final String name;
    private final ChatColor color;

    CosmeticRarity(String name, ChatColor color)
    {
        this.name = name;
        this.color = color;
    }

    public String getName()
    {
        return this.name;
    }

    public ChatColor getColor()
    {
        return this.color;
    }
}
