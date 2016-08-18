package net.samagames.hub.commands.moderating;

import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import net.samagames.hub.common.players.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CommandShutup extends AbstractCommand
{
    public CommandShutup(Hub hub)
    {
        super(hub);
    }

    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        boolean now = !this.hub.getChatManager().isChatLocked();
        this.hub.getChatManager().setChatLocked(now);

        if (now)
            this.hub.getServer().broadcastMessage(PlayerManager.MODERATING_TAG + ChatColor.RED + "Le chat a été fermé par un modérateur.");
        else
            this.hub.getServer().broadcastMessage(PlayerManager.MODERATING_TAG + ChatColor.GREEN + "Le chat a été réouvert.");

        return true;
    }
}
