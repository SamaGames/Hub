package net.samagames.hub.commands.animating;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.pearls.Pearl;
import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import net.samagames.hub.games.AbstractGame;
import net.samagames.persistanceapi.GameServiceManager;
import net.samagames.persistanceapi.beans.players.SanctionBean;
import net.samagames.tools.chat.ChatUtils;
import net.samagames.tools.chat.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 08/02/2017
 */
public class CommandEvent extends AbstractCommand
{
    private static final int[][] PRICES = new int[][] {{ 50, 0, 60 }, { 200, 0, 60*20 }, { 0, 1, 60*60 }, { 500, 1, 60*60*12 }, { 2000, 3, 60*60*24*5 }};

    public CommandEvent(Hub hub)
    {
        super(hub);
    }

    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        if (args.length < 1)
        {
            Jedis jedis = SamaGamesAPI.get().getBungeeResource();

            for (int i = 0; i < PRICES.length; i++)
                this.showPrices(jedis, player, null, null, i, PRICES[i][0], PRICES[i][1]);

            jedis.close();

            return true;
        }

        String subCommand = args[0];

        if (subCommand.equals("create"))
        {
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            player.sendMessage(ChatUtils.getCenteredText(ChatColor.WHITE + "♦ " + ChatColor.BOLD + "Assistant de création d'événement" + ChatColor.WHITE + " ♦"));
            player.sendMessage("");

            if (args.length == 5)
            {
                String gameCodeName = args[1];
                String map = args[2];
                int pricesId = Integer.parseInt(args[3]);

                Jedis jedis = SamaGamesAPI.get().getBungeeResource();

                if (jedis.exists("hub:event:cooldown:" + player.getUniqueId().toString() + ":" + pricesId))
                {
                    player.sendMessage("Impossible de créer l'événement : les gains choisis sont indisponibles pour le moment.");
                    jedis.close();

                    return true;
                }

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, PRICES[pricesId][2]);

                jedis.set("hub:event:selected:" + player.getUniqueId().toString(), String.valueOf(pricesId));
                jedis.set("hub:event:cooldown:" + player.getUniqueId().toString() + ":" + pricesId, String.valueOf(calendar.getTime().getTime()));
                jedis.expire("hub:event:cooldown:" + player.getUniqueId().toString() + ":" + pricesId, PRICES[pricesId][2]);

                jedis.close();

                try
                {
                    Class<?> coreAPIPluginClass = Class.forName("net.samagames.core.APIPlugin");
                    Field gameServiceManagerField = coreAPIPluginClass.getDeclaredField("gameServiceManager");
                    gameServiceManagerField.setAccessible(true);
                    GameServiceManager gameServiceManager = (GameServiceManager) gameServiceManagerField.get(this.hub.getServer().getPluginManager().getPlugin("SamaGamesAPI"));

                    gameServiceManager.applySanction(SanctionBean.TEXT, new SanctionBean(player.getUniqueId(), SanctionBean.TEXT, "Evénement créé : " + gameCodeName + ", " + map + ", " + PRICES[pricesId][0] + " pièces et " + PRICES[pricesId][1] + " perles.", null, false));
                }
                catch (Exception ignored) {}

                player.sendMessage(ChatColor.GREEN + "Votre serveur à été commandé. Veut sera téléporté dessus en tant que modérateur. Vous ne pourrez donc pas participer au jeu.");
                player.sendMessage(ChatColor.GREEN + "Votre gain est verouillé. La commande " + ChatColor.GOLD + "/event win <pseudo>" + ChatColor.GREEN + " donnera au joueur les gains sélectionnés.");

                String template = this.hub.getGameManager().getGameByIdentifier(gameCodeName).getGameSignsByMap(map).get(0).getTemplate();

                this.hub.getHydroangeasManager().orderServer(player.getName(), template);

                SamaGamesAPI.get().getPubSub().send("eventChannel", gameCodeName + ":" + template + ":" + PRICES[pricesId][0] + ":" + PRICES[pricesId][1]);
            }
            else if (args.length == 4)
            {
                String gameCodeName = args[1];
                String map = args[2];
                int pricesId = Integer.parseInt(args[3]);

                player.sendMessage(ChatUtils.getCenteredText(ChatColor.WHITE + "• Étape 4 : Confirmation •"));
                player.sendMessage("");

                player.sendMessage(ChatColor.WHITE + "- Jeu : " + ChatColor.GRAY + this.hub.getGameManager().getGameByIdentifier(gameCodeName).getName());
                player.sendMessage(ChatColor.WHITE + "- Carte : " + ChatColor.GRAY + map);

                int[] prices = PRICES[pricesId];
                String pricesLine = "";

                if (prices[0] > 0)
                    pricesLine += ChatColor.GOLD + "" + prices[0] + " pièce" + (prices[0] > 1 ? "s" : "");

                if (prices[0] > 0 && prices[1] > 0)
                    pricesLine += ChatColor.GRAY + " et ";

                if (prices[1] > 0)
                    pricesLine += ChatColor.GREEN + "" + prices[1] + " perle" + (prices[1] > 1 ? "s" : "");

                player.sendMessage(ChatColor.WHITE + "- Gains : " + pricesLine);
                player.sendMessage("");

                new FancyMessage("[Lancer l'événement]").color(ChatColor.GREEN).command("/event create " + gameCodeName + " " + map + " " + pricesId + " confirm").send(player);
            }
            else if (args.length == 3)
            {
                String gameCodeName = args[1];
                String template = args[2];

                player.sendMessage(ChatUtils.getCenteredText(ChatColor.WHITE + "• Étape 3 : Choix des gains •"));
                player.sendMessage("");

                Jedis jedis = SamaGamesAPI.get().getBungeeResource();

                for (int i = 0; i < PRICES.length; i++)
                    this.showPrices(jedis, player, gameCodeName, template, i, PRICES[i][0], PRICES[i][1]);

                jedis.close();
            }
            else if (args.length == 2)
            {
                String gameCodeName = args[1];

                player.sendMessage(ChatUtils.getCenteredText(ChatColor.WHITE + "• Étape 2 : Choix de la carte •"));
                player.sendMessage("");

                for (String map : this.hub.getGameManager().getGameByIdentifier(gameCodeName).getSigns().keySet())
                {
                    new FancyMessage("[\u25B6]").color(ChatColor.GREEN)
                            .command("/event create " + gameCodeName + " " + map)
                            .tooltip(ChatColor.GOLD + "» Clic pour sélectionner")
                            .then(" " + map).color(ChatColor.GRAY)
                            .send(player);
                }
            }
            else
            {
                player.sendMessage(ChatUtils.getCenteredText(ChatColor.WHITE + "• Étape 1 : Choix du jeu •"));
                player.sendMessage("");

                for (AbstractGame game : this.hub.getGameManager().getGames().values())
                {
                    if (game.getCodeName().equals("beta_vip") || game.getCodeName().equals("uhczone") || game.getCodeName().equals("event"))
                        continue;

                    new FancyMessage("[\u25B6]").color(ChatColor.GREEN)
                            .command("/event create " + game.getCodeName())
                            .tooltip(ChatColor.GOLD + "» Clic pour sélectionner")
                            .then(" " + game.getName()).color(ChatColor.GRAY)
                            .send(player);
                }
            }

            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        }
        else if (subCommand.equals("win"))
        {
            if (args.length != 2)
            {
                this.showHelp(player);
                return true;
            }

            String playerName = args[1];

            if (this.hub.getServer().getPlayer(playerName) == null)
            {
                player.sendMessage(ChatColor.RED + "Joueur introuvable sur votre serveur.");
                return true;
            }

            Jedis jedis = SamaGamesAPI.get().getBungeeResource();

            if(!jedis.exists("hub:event:selected:" + player.getUniqueId().toString()))
            {
                player.sendMessage(ChatColor.RED + "Vous n'avez aucun événement en cours.");
                return true;
            }

            int pricesId = Integer.parseInt(jedis.get("hub:event:selected:" + player.getUniqueId().toString()));

            if (PRICES[pricesId][0] > 0)
                SamaGamesAPI.get().getPlayerManager().getPlayerData(this.hub.getServer().getPlayer(playerName).getUniqueId()).creditCoins(PRICES[pricesId][0], "Evénement", false);

            if (PRICES[pricesId][1] > 0)
            {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, 7);

                for (int i = 0; i < PRICES[pricesId][1]; i++)
                    this.hub.getInteractionManager().getWellManager().addPearlToPlayer(this.hub.getServer().getPlayer(playerName), new Pearl(UUID.randomUUID(), new Random().nextInt(4) + 1, calendar.getTime().getTime()));

                this.hub.getScoreboardManager().update(this.hub.getServer().getPlayer(playerName));
                this.hub.getInteractionManager().getGraouManager().update(this.hub.getServer().getPlayer(playerName));
            }

            jedis.del("hub:event:selected:" + player.getUniqueId().toString());

            player.sendMessage(ChatColor.GREEN + "Le joueur a bien été crédité de ses gains. L'événement est marqué comme terminé.");
        }
        else
        {
            this.showHelp(player);
        }

        return true;
    }

    public void showPrices(Jedis jedis, Player player, String gameCodeName, String template, int id, int coins, int pearls)
    {
        boolean currentlyCooldown = jedis.exists("hub:event:cooldown:" + player.getUniqueId().toString() + ":" + id);

        FancyMessage message = new FancyMessage("[\u25B6]").color(currentlyCooldown ? ChatColor.RED : ChatColor.GREEN);

        if (!currentlyCooldown && gameCodeName != null)
        {
            message.command("/event create " + gameCodeName + " " + template + " " + id);
            message.tooltip(ChatColor.GOLD + "» Clic pour sélectionner");
        }
        else if (currentlyCooldown)
        {
            message.tooltip(ChatColor.RED + "» Disponible dans " + this.formatCooldownDate(Long.parseLong(jedis.get("hub:event:cooldown:" + player.getUniqueId().toString() + ":" + id))));
        }
        else
        {
            message.tooltip(ChatColor.GREEN + "» Disponible");
        }

        message.then(" ");

        if (coins > 0)
            message.then(coins + " pièce" + (coins > 1 ? "s" : "")).color(ChatColor.GOLD);

        if (coins > 0 && pearls > 0)
            message.then(" et ").color(ChatColor.GRAY);

        if (pearls > 0)
            message.then(pearls + " perle" + (pearls > 1 ? "s" : "")).color(ChatColor.GREEN);

        message.send(player);
    }

    public void showHelp(Player player)
    {
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        player.sendMessage(ChatUtils.getCenteredText(ChatColor.WHITE + "♦ " + ChatColor.BOLD + "SamaGames - Aide - Evénement" + ChatColor.WHITE + " ♦"));
        player.sendMessage("");

        this.showHelpSubCommand(player, "create", "Créer un événement pas à pas");
        this.showHelpSubCommand(player, "win", "Déclarer gagnant un joueur et lui donner sa récompense");

        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
    }

    private void showHelpSubCommand(Player player, String command, String description)
    {
        new FancyMessage("/event " + command).color(ChatColor.YELLOW)
                .command("/event " + command)
                .tooltip(ChatColor.GOLD + "» Clic pour pré-remplir la commande")
                .then(" : " + description).color(ChatColor.GRAY)
                .send(player);
    }

    private String formatCooldownDate(long endTime)
    {
        long delta = endTime - System.currentTimeMillis();

        long days = TimeUnit.MILLISECONDS.toDays(delta);
        delta -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(delta);
        delta -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(delta);
        delta -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(delta);

        String ret = "";
        if (days > 0)
            ret += days + " jours ";

        if (hours > 0)
            ret += hours + " heures ";

        if (minutes > 0)
            ret += minutes + " minutes ";

        if (seconds > 0)
            ret += seconds + " secondes";

        if (ret.isEmpty() && minutes == 0)
            ret += "moins d'une minute";

        return ret;
    }
}
