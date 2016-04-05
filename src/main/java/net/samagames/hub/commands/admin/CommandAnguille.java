package net.samagames.hub.commands.admin;

import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import net.samagames.hub.common.players.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CommandAnguille extends AbstractCommand
{
    public CommandAnguille(Hub hub)
    {
        super(hub);
    }

    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        if(this.hub.getPlayerManager().canBuild())
        {
            this.hub.getPlayerManager().setBuild(false);
            player.sendMessage(PlayerManager.SETTINGS_TAG + ChatColor.RED + "Construction désactivée.");
        }
        else
        {
            this.hub.getPlayerManager().setBuild(true);
            player.sendMessage(PlayerManager.SETTINGS_TAG + ChatColor.GREEN + "Construction activée.");
        }

        return true;
    }
}
