package net.samagames.hub.commands.moderating;

import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CommandSlow extends AbstractCommand
{
    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        if(args.length == 1)
        {
            if(StringUtils.isNumeric(args[0]))
            {
                int slow = Integer.parseInt(args[0]);

                if(slow > 300 || slow < 0)
                {
                    player.sendMessage(ChatColor.RED + "Espacement incorrect ! (Trop grand ou négatif)");
                    return true;
                }

                Hub.getInstance().getChatManager().setActualSlowDuration(slow);

                if(slow == 0)
                    Bukkit.broadcastMessage(ChatColor.GOLD + "Le chat n'est plus ralenti.");
                else
                    Bukkit.broadcastMessage(ChatColor.GOLD + "Un modérateur a ralenti le chat. Ecart minimum entre deux messages : " + ChatColor.AQUA + slow + " secondes.");
            }
            else
            {
                player.sendMessage(ChatColor.RED + "Veuillez entrer un nombre correct !");
            }
        }
        else
        {
            player.sendMessage(ChatColor.GREEN + "Rélentissement actuel du chat : " + ChatColor.GOLD + Hub.getInstance().getChatManager().getActualSlowDuration());
        }

        return true;
    }
}
