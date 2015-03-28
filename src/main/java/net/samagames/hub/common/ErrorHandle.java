package net.samagames.hub.common;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ErrorHandle implements Thread.UncaughtExceptionHandler
{
    @Override
    public void uncaughtException(Thread thread, Throwable e)
    {
        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "Une erreur s'est produite sur ce serveur ! Le fonctionnement de celui-ci a pu être altéré !");
        Bukkit.broadcastMessage(ChatColor.RED + "(" + e.getMessage() + ")");
        e.printStackTrace();
    }
}
