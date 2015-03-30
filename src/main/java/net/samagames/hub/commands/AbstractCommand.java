package net.samagames.hub.commands;

import net.samagames.permissionsbukkit.PermissionsBukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class AbstractCommand implements CommandExecutor
{
    private final String permission;

    public AbstractCommand(String permission)
    {
        this.permission = permission;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
    {
        if(!(commandSender instanceof Player))
        {
            commandSender.sendMessage(ChatColor.RED + "Vous devez être un joueur en jeu pour pouvoir utiliser cette commande.");
            return true;
        }

        if(this.permission != null)
        {
            if(!hasPermission((Player) commandSender))
            {
                commandSender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande.");
                return true;
            }
        }

        return this.doAction((Player) commandSender, command, s, args);
    }

    public abstract boolean doAction(Player player, Command command, String s, String[] args);

    public boolean hasPermission(Player player)
    {
        return PermissionsBukkit.hasPermission(player, this.permission);
    }
}
