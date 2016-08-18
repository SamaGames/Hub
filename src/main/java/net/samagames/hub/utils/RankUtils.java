package net.samagames.hub.utils;

import net.md_5.bungee.api.ChatColor;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.permissions.IPermissionsEntity;

import java.util.UUID;

public class RankUtils
{
    public static String getFormattedRank(UUID uuid, boolean overrideNickname)
    {
        IPermissionsEntity permissionsEntity = SamaGamesAPI.get().getPermissionsManager().getPlayer(uuid);

        String prefix = overrideNickname ? permissionsEntity.getPrefix() : permissionsEntity.getDisplayPrefix();
        String display = (overrideNickname ? permissionsEntity.getTag() : permissionsEntity.getDisplayTag()).replace("[", "").replace("]", "");

        if (ChatColor.stripColor(display).isEmpty())
            display = ChatColor.GRAY + "Joueur";

        return prefix + display;
    }
}
