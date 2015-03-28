package net.samagames.hub.utils;

import net.samagames.permissionsbukkit.PermissionsBukkit;
import org.bukkit.ChatColor;

import java.util.UUID;

public class RankUtils
{
    public static String getFormattedRank(UUID uuid)
    {
        String prefix = PermissionsBukkit.getPrefix(PermissionsBukkit.getApi().getUser(uuid));
        String display = PermissionsBukkit.getDisplay(PermissionsBukkit.getApi().getUser(uuid)).replace("[", "").replace("]", "");

        if(ChatColor.stripColor(display).isEmpty())
            display = ChatColor.GRAY + "Joueur";

        return prefix + display;
    }
}
