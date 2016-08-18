package net.samagames.hub.common.managers;

import org.bukkit.entity.Player;

interface EntryPoints
{
    void onDisable();
    void onLogin(Player player);
    void onLogout(Player player);
}
