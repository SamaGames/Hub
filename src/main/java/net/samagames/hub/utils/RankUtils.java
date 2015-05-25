package net.samagames.hub.utils;

import net.samagames.api.SamaGamesAPI;
import org.bukkit.ChatColor;

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
