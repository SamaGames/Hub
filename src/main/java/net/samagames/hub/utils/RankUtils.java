package net.samagames.hub.utils;

import net.md_5.bungee.api.ChatColor;
import net.samagames.api.SamaGamesAPI;

import java.util.UUID;

public class RankUtils
{
    public static String getFormattedRank(UUID uuid)
    {
        String prefix = SamaGamesAPI.get().getPermissionsManager().getPrefix(SamaGamesAPI.get().getPermissionsManager().getApi().getUser(uuid));
        String display = SamaGamesAPI.get().getPermissionsManager().getDisplay(SamaGamesAPI.get().getPermissionsManager().getApi().getUser(uuid)).replace("[", "").replace("]", "");

        if(ChatColor.stripColor(display).isEmpty())
            display = ChatColor.GRAY + "Joueur";

        return prefix + display;
    }
}
