package net.samagames.hub.commands.admin;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.sign.GameSign;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

public class CommandSign extends AbstractCommand
{
    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        if(args.length < 1)
        {
            player.sendMessage(ChatColor.RED + "Usage: /sign <add|maintenance|reload|list> <...>");
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

            case "reload":
                Hub.getInstance().getSignManager().reloadList();
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
        if(args.length < 5)
        {
            player.sendMessage(ChatColor.RED + "Usage: /sign add <game code name> <map> <color [default GREEN]> <template>");
            return;
        }

        if(Hub.getInstance().getPlayerManager().getSelection(player) == null)
        {
            player.sendMessage(ChatColor.RED + "Vous devez sélectionner une zone pour l'ajouter.");
            return;
        }

        Location selection = Hub.getInstance().getPlayerManager().getSelection(player);

        String game = args[1];
        String map = args[2];
        ChatColor color = ChatColor.valueOf(args[3].toUpperCase());
        String template = args[4];

        if(!(selection.getBlock().getState() instanceof Sign))
        {
            player.sendMessage(ChatColor.RED + "Le bloc sélectionné n'est pas un panneau !");
            return;
        }

        Sign sign = (Sign) selection.getBlock().getState();
        Hub.getInstance().getSignManager().setSignForMap(player, game, map, color, template, sign);
    }

    private void maintenanceSigns(Player player, String[] args)
    {
        if(args.length < 3)
        {
            player.sendMessage(ChatColor.RED + "Usage: /sign maintenance <game> <template>");
            return;
        }

        String game = args[1];
        String template = args[2];

        AbstractGame gameObject = Hub.getInstance().getGameManager().getGameByIdentifier(game);

        if(gameObject == null)
        {
            player.sendMessage(ChatColor.RED + "Jeu non trouvé :(");
            return;
        }

        GameSign gameSign = gameObject.getGameSignByTemplate(template);

        if(gameSign == null)
        {
            player.sendMessage(ChatColor.RED + "Template non trouvé :(");
            return;
        }

        Jedis jedis = SamaGamesAPI.get().getBungeeResource();
        jedis.set("hub:maintenance:" + game + ":" + template, String.valueOf(!gameSign.isMaintenance()));
        jedis.close();

        gameSign.setMaintenance(!gameSign.isMaintenance());
        SamaGamesAPI.get().getPubSub().send("maintenanceSignChannel", game + ":" + !gameSign.isMaintenance());
    }

    private void listSigns(Player player)
    {
        player.sendMessage(ChatColor.GREEN + "Liste des zones :");

        for(AbstractGame game : Hub.getInstance().getGameManager().getGames().values())
        {
            player.sendMessage(ChatColor.GREEN + "-> " + ChatColor.AQUA + game.getCodeName() + ChatColor.GREEN + " (" + ChatColor.AQUA + game.getSigns().values().size() + " panneaux" + ChatColor.GREEN + ")");
        }
    }
}
