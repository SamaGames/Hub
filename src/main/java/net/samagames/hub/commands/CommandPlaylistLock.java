package net.samagames.hub.commands;

import net.samagames.hub.Hub;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CommandPlaylistLock extends AbstractCommand
{
    public CommandPlaylistLock()
    {
        super("hub.jukebox.lock");
    }

    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        Hub.getInstance().getCosmeticManager().getJukeboxManager().toggleLock();
        return false;
    }
}
