package net.samagames.hub.commands.moderating;

import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CommandPlaylistClear extends AbstractCommand
{
    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        Hub.getInstance().getCosmeticManager().getJukeboxManager().clear();
        return true;
    }
}
