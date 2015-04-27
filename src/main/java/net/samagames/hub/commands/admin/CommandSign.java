package net.samagames.hub.commands.admin;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.sign.GameSign;
import net.samagames.tools.Selection;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

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
                this.maintenanceSigns(player, args);
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

        if(!(selection.getMinimumPoint().getBlock().getState() instanceof Sign))
        {
            player.sendMessage(ChatColor.RED + "Le bloc sélectionné n'est pas un panneau !");
            return;
        }

        Sign sign = (Sign) selection.getMinimumPoint().getBlock().getState();
        Hub.getInstance().getSignManager().setSignForMap(player, game, map, sign);
    }

    private void maintenanceSigns(Player player, String[] args)
    {
        if(args.length < 2)
        {
            player.sendMessage(ChatColor.RED + "Usage: /sign maintenance <game>");
            return;
        }

        String game = args[1];

        AbstractGame gameObject = Hub.getInstance().getGameManager().getGameByIdentifier(game);

        if(gameObject == null)
        {
            player.sendMessage(ChatColor.RED + "Jeu non trouvé :(");
            return;
        }

        SamaGamesAPI.get().getPubSub().send("maintenanceSignChannel", game + ":" + !gameObject.isMaintenance());
    }

    private void listSigns(Player player)
    {
        player.sendMessage(ChatColor.GREEN + "Liste des zones :");

        for(AbstractGame game : Hub.getInstance().getGameManager().getGames().values())
        {
            int i = 0;

            for(GameSign sign : game.getSigns().values())
                i++;

            player.sendMessage(ChatColor.GREEN + "-> " + ChatColor.AQUA + game.getCodeName() + ChatColor.GREEN + " (" + ChatColor.AQUA + i + " panneaux" + ChatColor.GREEN + ")");
        }
    }
}
