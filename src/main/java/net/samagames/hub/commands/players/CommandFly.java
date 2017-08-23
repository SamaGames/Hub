package net.samagames.hub.commands.players;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import net.samagames.hub.common.players.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
public class CommandFly extends AbstractCommand
{
    public CommandFly(Hub hub)
    {
        super(hub);
    }

    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        if (!SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "hub.fly"))
        {
            player.sendMessage(PlayerManager.SETTINGS_TAG + ChatColor.RED + "Vous n'avez pas le grade nécéssaire pour utiliser cette commande.");
            return true;
        }

        if (player.getGameMode() != GameMode.ADVENTURE || this.hub.getPlayerManager().isBusy(player))
        {
            player.sendMessage(PlayerManager.SETTINGS_TAG + ChatColor.RED + "Vous ne pouvez pas utiliser cette commande actuellement.");
            return true;
        }

        boolean now = !player.getAllowFlight();
        player.setAllowFlight(now);

        if (now)
            player.sendMessage(PlayerManager.SETTINGS_TAG + ChatColor.GREEN + "Vous pouvez maintenant voler.");
        else
            player.sendMessage(PlayerManager.SETTINGS_TAG + ChatColor.GREEN + "Vous ne pouvez plus voler.");

        return true;
    }
}
