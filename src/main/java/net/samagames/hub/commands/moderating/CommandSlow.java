package net.samagames.hub.commands.moderating;

import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import net.samagames.hub.common.players.PlayerManager;
import org.apache.commons.lang.StringUtils;
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
public class CommandSlow extends AbstractCommand
{
    public CommandSlow(Hub hub)
    {
        super(hub);
    }

    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        if (args.length == 1)
        {
            if (StringUtils.isNumeric(args[0]))
            {
                int slow = Integer.parseInt(args[0]);

                if (slow > 300 || slow < 0)
                {
                    player.sendMessage(PlayerManager.MODERATING_TAG + ChatColor.RED + "Espacement incorrect ! (Trop grand ou négatif)");
                    return true;
                }

                this.hub.getChatManager().setActualSlowDuration(slow);

                if (slow == 0)
                    this.hub.getServer().broadcastMessage(PlayerManager.MODERATING_TAG + ChatColor.GREEN + "Le chat n'est plus ralenti.");
                else
                    this.hub.getServer().broadcastMessage(PlayerManager.MODERATING_TAG + ChatColor.YELLOW + "Un modérateur a ralenti le chat. Ecart minimum entre deux messages : " + ChatColor.AQUA + slow + " secondes" + ChatColor.YELLOW + ".");
            }
            else
            {
                player.sendMessage(PlayerManager.MODERATING_TAG + ChatColor.RED + "Veuillez entrer un nombre correct !");
            }
        }
        else
        {
            player.sendMessage(PlayerManager.MODERATING_TAG + ChatColor.GREEN + "Ralentissement actuel du chat : " + ChatColor.GOLD + this.hub.getChatManager().getActualSlowDuration());
        }

        return true;
    }
}
