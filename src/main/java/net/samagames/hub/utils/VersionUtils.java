package net.samagames.hub.utils;

import org.bukkit.entity.Player;
import protocolsupport.api.ProtocolSupportAPI;

public class VersionUtils
{
    public static boolean isLoggedInPost19(Player player)
    {
        return ProtocolSupportAPI.getProtocolVersion(player).getId() >= 107;
    }
}
