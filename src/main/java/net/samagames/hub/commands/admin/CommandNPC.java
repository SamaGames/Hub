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
            player.sendMessage(ChatColor.RED + "Usage: /npc <debug|reload|list>");
            return true;
        }

        String commandName = args[0];

        switch(commandName)
        {
            case "debug":
                this.toggleDebug(player, args);
                break;

            case "reload":
                Hub.getInstance().getNPCManager().reloadNPCS();
                break;

            case "list":
                Hub.getInstance().getNPCManager().listNPCS(player);
                break;
        }

        return true;
    }

    private void toggleDebug(Player sender, String[] args)
    {
        if(args.length < 1)
        {
            sender.sendMessage(ChatColor.RED + "Usage: /npc debug <true/false>");
            return;
        }
        else if(!args[1].equals("false") && !args[1].equals("true"))
        {
            sender.sendMessage(ChatColor.RED + "Usage: /npc debug <true/false>");
            return;
        }

        boolean flag = Boolean.valueOf(args[1]);
        Hub.getInstance().getNPCManager().toggleDebug(flag);
        sender.sendMessage(ChatColor.GREEN + "Le mode débug a été basculé sur '" + flag + "' !");
    }
}
