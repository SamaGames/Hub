package net.samagames.hub.commands;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.jukebox.JukeboxPlaylist;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CommandMeh extends AbstractCommand
{
    public CommandMeh()
    {
        super(null);
    }

    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        JukeboxPlaylist current = Hub.getInstance().getCosmeticManager().getJukeboxManager().getCurrentSong();
        String tag = Hub.getInstance().getCosmeticManager().getJukeboxManager().getJukeboxTag();

        if (current == null)
        {
            player.sendMessage(tag + ChatColor.RED + "Aucun son n'est lu actuellement.");
        }
        else
        {
            if (!current.meh(player))
                player.sendMessage(tag + ChatColor.RED + "Vous avez déjà voté Meh pour cette musique.");
            else
                player.sendMessage(tag + ChatColor.GREEN + "Vous avez bien indiqué que cette musique ne vous plait pas.");
        }

        return true;
    }
}
