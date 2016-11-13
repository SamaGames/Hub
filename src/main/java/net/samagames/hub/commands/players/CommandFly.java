package net.samagames.hub.commands.players;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import net.samagames.hub.common.players.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 13/11/2016
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
