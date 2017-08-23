package net.samagames.hub.commands.players;

import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import net.samagames.hub.cosmetics.jukebox.JukeboxManager;
import net.samagames.hub.cosmetics.jukebox.JukeboxSong;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/*
 * This file is part of Hub.
 *
 * Hub is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hub is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hub.  If not, see <http://www.gnu.org/licenses/>.
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
