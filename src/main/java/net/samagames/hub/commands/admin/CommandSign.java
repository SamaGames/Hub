package net.samagames.hub.commands.admin;

import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.sign.GameSign;
import net.samagames.hub.games.sign.GameSignZone;
import net.samagames.tools.Selection;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandSign extends AbstractCommand
{
    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        if(args.length < 1)
        {
            player.sendMessage(ChatColor.RED + "Usage: /sign <add|maintenance|list> <...>");
            return true;
        }

        switch(args[0])
        {
            case "add":
                this.addSign(player, args);
                break;

            case "maintenance":
                this.maintenanceSigns();
                break;

            case "list":
                this.listSigns(player);
                break;

            default:
                player.sendMessage(ChatColor.RED + "Commande inconnue !");
                break;
        }

        return true;
    }

    private void addSign(Player player, String[] args)
    {
        if(args.length < 3)
        {
            player.sendMessage(ChatColor.RED + "Usage: /sign add <game code name> <map>");
            return;
        }

        if(Hub.getInstance().getPlayerManager().getSelection(player) == null)
        {
            player.sendMessage(ChatColor.RED + "Vous devez sélectionner une zone pour l'ajouter.");
            return;
        }

        Selection selection = Hub.getInstance().getPlayerManager().getSelection(player);

        String game = args[1];
        String map = args[2];
        ArrayList<Sign> signs = new ArrayList<>();

        for(int x = selection.getMinimumPoint().getBlockX(); x <= selection.getMaximumPoint().getBlockX(); x++)
        {
            for(int y = selection.getMinimumPoint().getBlockY(); y <= selection.getMaximumPoint().getBlockY(); y++)
            {
                for(int z = selection.getMinimumPoint().getBlockZ(); z <= selection.getMaximumPoint().getBlockZ(); z++)
                {
                    Block block = Hub.getInstance().getHubWorld().getBlockAt(x, y, z);

                    if(block.getType() == Material.SIGN || block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN)
                        signs.add((Sign) block.getState());
                }
            }
        }

        Hub.getInstance().getSignManager().addZone(player, game, map, signs);
    }

    private void maintenanceSigns()
    {

    }

    private void listSigns(Player player)
    {
        player.sendMessage(ChatColor.GREEN + "Liste des zones :");

        for(AbstractGame game : Hub.getInstance().getGameManager().getGames().values())
        {
            int i = 0;

            for(GameSignZone zone : game.getSignZones().values())
            {
                for(GameSign sign : zone.getSigns())
                {
                    i++;
                }
            }

            player.sendMessage(ChatColor.GREEN + "-> " + ChatColor.AQUA + game.getCodeName() + ChatColor.GREEN + " (" + ChatColor.AQUA + i + " panneaux" + ChatColor.GREEN + ")");
        }
    }
}
