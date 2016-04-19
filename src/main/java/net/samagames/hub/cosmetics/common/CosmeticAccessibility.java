package net.samagames.hub.cosmetics.common;

import net.samagames.api.SamaGamesAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
