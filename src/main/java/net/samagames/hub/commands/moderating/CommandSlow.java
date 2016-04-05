package net.samagames.hub.commands.moderating;

import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import net.samagames.hub.common.players.PlayerManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CommandSlow extends AbstractCommand
{
    public CommandSlow(Hub hub)
    {
        super(hub);
    }

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
                    player.sendMessage(PlayerManager.MODERATING_TAG + ChatColor.RED + "Espacement incorrect ! (Trop grand ou négatif)");
                    return true;
                }

                this.hub.getChatManager().setActualSlowDuration(slow);

                if(slow == 0)
                    this.hub.getServer().broadcastMessage(PlayerManager.MODERATING_TAG + ChatColor.GREEN + "Le chat n'est plus ralenti.");
                else
                    this.hub.getServer().broadcastMessage(PlayerManager.MODERATING_TAG + ChatColor.YELLOW + "Un modérateur a ralenti le chat. Ecart minimum entre deux messages : " + ChatColor.AQUA + slow + " secondes" + ChatColor.YELLOW + ".");
            }
            else
            {
                player.sendMessage(PlayerManager.MODERATING_TAG + ChatColor.RED + "Veuillez entrer un nombre correct !");
            }
        }
        else
        {
            player.sendMessage(PlayerManager.MODERATING_TAG + ChatColor.GREEN + "Ralentissement actuel du chat : " + ChatColor.GOLD + this.hub.getChatManager().getActualSlowDuration());
        }

        return true;
    }
}
