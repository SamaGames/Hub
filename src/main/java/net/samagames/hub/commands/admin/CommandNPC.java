package net.samagames.hub.commands.admin;

import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CommandNPC extends AbstractCommand
{
    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        if (args == null || args.length < 1)
        {
            player.sendMessage(ChatColor.RED + "Usage: /npc <reload|list>");
            return true;
        }

        String commandName = args[0];

        switch(commandName)
        {
            case "reload":
                Hub.getInstance().getNPCManager().reloadNPCS();
                break;

            case "list":
                Hub.getInstance().getNPCManager().listNPCS(player);
                break;
        }

        return true;
    }
}
