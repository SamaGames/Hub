package net.samagames.hub.commands;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
public abstract class AbstractCommand implements CommandExecutor
{
    protected final Hub hub;
    private String permission;

    public AbstractCommand(Hub hub)
    {
        this.hub = hub;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
    {
        if (!(commandSender instanceof Player))
        {
            commandSender.sendMessage(ChatColor.RED + "Vous devez être un joueur en jeu pour pouvoir utiliser cette commande.");
            return true;
        }

        if (this.permission != null)
        {
            if (!hasPermission((Player) commandSender))
            {
                commandSender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande.");
                return true;
            }
        }

        return this.doAction((Player) commandSender, command, s, args);
    }

    public abstract boolean doAction(Player player, Command command, String s, String[] args);

    public void setPermission(String permission)
    {
        this.permission = permission;
    }

    protected boolean hasPermission(Player player)
    {
        return SamaGamesAPI.get().getPermissionsManager().hasPermission(player, this.permission);
    }
}
