package net.samagames.hub.commands.moderating;

import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CommandShutup extends AbstractCommand
{
    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        boolean now = !Hub.getInstance().getChatManager().canChat();
        Hub.getInstance().getChatManager().setChatEnabled(now);

        if(now)
            Bukkit.broadcastMessage(ChatColor.GOLD + "Le chat a été réouvert.");
        else
            Bukkit.broadcastMessage(ChatColor.GOLD + "Le chat a été fermé par un modérateur.");

        return true;
    }
}
