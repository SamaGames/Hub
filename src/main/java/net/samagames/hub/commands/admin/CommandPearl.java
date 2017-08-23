package net.samagames.hub.commands.admin;

import net.samagames.api.games.pearls.Pearl;
import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.UUID;

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
public class CommandPearl extends AbstractCommand
{
    public CommandPearl(Hub hub)
    {
        super(hub);
    }

    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        if (args.length == 2)
        {
            try
            {
                String playerName = args[0];
                int pearlLevel = Integer.parseInt(args[1]);

                if (pearlLevel < 1 || pearlLevel > 5)
                {
                    player.sendMessage(ChatColor.RED + "Veuillez indiquer un niveau de perle valide (1 à 5).");
                    return true;
                }

                Player p = this.hub.getServer().getPlayer(playerName);

                if (p == null)
                {
                    player.sendMessage(ChatColor.RED + "Le joueur indiqué est introuvable.");
                    return true;
                }

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, 60);

                this.hub.getInteractionManager().getWellManager().addPearlToPlayer(p, new Pearl(UUID.randomUUID(), pearlLevel, calendar.getTime().getTime()));
                player.sendMessage(ChatColor.GREEN + "Une perle de niveau " + ChatColor.GOLD + pearlLevel + ChatColor.GREEN + " à été donnée au joueur " + ChatColor.GOLD + playerName + ChatColor.GREEN + ". Elle sera valide durant les 60 prochaines minutes.");

                this.hub.getScoreboardManager().update(p);
                this.hub.getInteractionManager().getGraouManager().update(p);
            }
            catch (NumberFormatException e)
            {
                player.sendMessage(ChatColor.RED + "Veuillez indiquer un niveau de perle valide (1 à 5).");
            }
        }
        else
        {
            player.sendMessage(ChatColor.RED + "Usage: /pearl <player> <level>");
        }

        return true;
    }
}
