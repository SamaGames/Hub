package net.samagames.hub.commands.moderating;

import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.cosmetics.CosmeticManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 * Created by Rigner for project Hub.
 */
public class CommandPlaylistLock extends AbstractCommand
{
    public CommandPlaylistLock(Hub hub)
    {
        super(hub);
    }

    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        this.hub.getCosmeticManager().getJukeboxManager().toggleLock();
        this.hub.getServer().broadcastMessage(PlayerManager.MODERATING_TAG + ChatColor.RED + "La playlist du jukebox a été bloquée par un modérateur.");

        return true;
    }
}
