package net.samagames.hub.commands.admin;

import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CommandAnguille extends AbstractCommand
{
    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        if(Hub.getInstance().getPlayerManager().canBuild())
        {
            Hub.getInstance().getPlayerManager().setBuildEnabled(false);
            player.sendMessage(ChatColor.GREEN + "Construction désactivée.");
        }
        else
        {
            Hub.getInstance().getPlayerManager().setBuildEnabled(true);
            player.sendMessage(ChatColor.GREEN + "Construction activée.");
        }

        return true;
    }
}
