package net.samagames.hub.commands.players;

import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import net.samagames.hub.cosmetics.jukebox.JukeboxManager;
import net.samagames.hub.cosmetics.jukebox.JukeboxSong;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 * Created by Rigner for project Hub.
 */
public class CommandMeh extends AbstractCommand
{
    public CommandMeh(Hub hub)
    {
        super(hub);
    }

    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        JukeboxSong current = this.hub.getCosmeticManager().getJukeboxManager().getCurrentSong();
        String tag = JukeboxManager.JUKEBOX_TAG;

        if (current == null)
        {
            player.sendMessage(tag + ChatColor.RED + "Aucun son n'est lu actuellement.");
        }
        else
        {
            if (current.getPlayedBy().equals(player.getName()))
                player.sendMessage(tag + ChatColor.RED + "Vous ne pouvez pas voter sur votre musique.");
            else if (!current.meh(player))
                player.sendMessage(tag + ChatColor.RED + "Vous avez déjà voté Meh pour cette musique.");
            else
                player.sendMessage(tag + ChatColor.GREEN + "Vous avez bien indiqué que cette musique ne vous plait pas.");
        }

        return true;
    }
}
