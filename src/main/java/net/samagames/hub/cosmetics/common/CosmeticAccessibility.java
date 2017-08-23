package net.samagames.hub.cosmetics.common;

import net.samagames.api.SamaGamesAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
public enum CosmeticAccessibility
{
    ALL(null, null),
    VIP(ChatColor.GREEN + "VIP", "network.vip"),
    VIPPLUS(ChatColor.AQUA + "VIP" + ChatColor.LIGHT_PURPLE + "+", "network.vipplus"),
    STAFF(ChatColor.RED + "Staff", "network.staff"),
    ADMIN(ChatColor.RED + "Administrateur", "network.admin"),
    ;

    private final String display;
    private final String permission;

    CosmeticAccessibility(String display, String permission)
    {
        this.display = display;
        this.permission = permission;
    }

    public String getDisplay()
    {
        return this.display;
    }

    public boolean canAccess(Player player)
    {
        return this.permission == null || SamaGamesAPI.get().getPermissionsManager().hasPermission(player, this.permission);
    }
}
