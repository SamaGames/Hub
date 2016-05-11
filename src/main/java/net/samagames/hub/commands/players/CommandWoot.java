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
public class CommandWoot extends AbstractCommand
{
    public CommandWoot(Hub hub)
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
            if (!current.woot(player))
                player.sendMessage(tag + ChatColor.RED + "Vous avez déjà voté Woot pour cette musique.");
            else
                player.sendMessage(tag + ChatColor.GREEN + "Vous avez marqué votre appréciation pour la musique jouée.");
        }

        return true;
    }
}
