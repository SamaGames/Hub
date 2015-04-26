package net.samagames.hub.commands.eastereggs;

import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CommandPacman extends AbstractCommand
{
    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        if(!player.getName().equals("IamBlueSlime"))
        {
            player.sendMessage(ChatColor.RED + "Macoum macoum !");
            return true;
        }

        if(args.length >= 1)
        {
            switch(args[0])
            {
                case "on":
                    this.startPacman(player);
                    break;

                case "off":
                    this.stopPacman(player);
                    break;
            }
        }
        else
        {
            player.sendMessage(ChatColor.RED + "Usage: /pacman <on|off>");
        }

        return true;
    }

    private void startPacman(Player player)
    {
        if(!Hub.getInstance().getSignManager().isPacmanEnabled())
        {
            Hub.getInstance().getSignManager().startPacman(player);
            player.sendMessage(ChatColor.GREEN + "Pacman démarré.");
        }
        else
        {
            player.sendMessage(ChatColor.RED + "Pacman est déjà présent.");
        }
    }

    private void stopPacman(Player player)
    {
        if(Hub.getInstance().getSignManager().isPacmanEnabled())
        {
            Hub.getInstance().getSignManager().stopPacman();
            player.sendMessage(ChatColor.GREEN + "Pacman stoppé.");
        }
        else
        {
            player.sendMessage(ChatColor.RED + "Pacman n'est actuellement pas présent.");
        }
    }
}
