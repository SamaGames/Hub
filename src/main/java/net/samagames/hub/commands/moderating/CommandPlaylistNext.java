package net.samagames.hub.commands.moderating;

import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
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
public class CommandPlaylistNext extends AbstractCommand
{
    public CommandPlaylistNext(Hub hub)
    {
        super(hub);
    }

    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        this.hub.getCosmeticManager().getJukeboxManager().skipSong(false);
        return true;
    }
}
