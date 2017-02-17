package net.samagames.hub.commands.animating;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.pearls.Pearl;
import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import net.samagames.hub.games.AbstractGame;
import net.samagames.persistanceapi.GameServiceManager;
import net.samagames.persistanceapi.beans.events.EventBean;
import net.samagames.persistanceapi.beans.events.EventWinnerBean;
import net.samagames.persistanceapi.beans.players.SanctionBean;
import net.samagames.tools.chat.ChatUtils;
import net.samagames.tools.chat.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Field;
import java.sql.Timestamp;
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
    private static final int[][] REWARDS = new int[][] {{ 50, 0, 60 }, { 200, 0, 60*20 }, { 0, 1, 60*60 }, { 500, 1, 60*60*12 }, { 2000, 3, 60*60*24*5 }};

    public CommandEvent(Hub hub)
    {
        super(hub);
    }

    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        if (args.length < 1)
        {
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            player.sendMessage(ChatUtils.getCenteredText(ChatColor.WHITE + "♦ " + ChatColor.BOLD + "Récompenses disponibles" + ChatColor.WHITE + " ♦"));
            player.sendMessage("");

            Jedis jedis = SamaGamesAPI.get().getBungeeResource();

            for (int i = 0; i < REWARDS.length; i++)
                this.showPrices(jedis, player, null, null, i, REWARDS[i][0], REWARDS[i][1]);

            jedis.close();

            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

            return true;
        }

        String subCommand = args[0];

        if (subCommand.equals("create"))
        {
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            player.sendMessage(ChatUtils.getCenteredText(ChatColor.WHITE + "♦ " + ChatColor.BOLD + "Assistant de création d'événement" + ChatColor.WHITE + " ♦"));
            player.sendMessage("");

            Jedis jedis = SamaGamesAPI.get().getBungeeResource();
            boolean ongoing = jedis.exists("hub:event:current:" + player.getUniqueId().toString());
            jedis.close();

            if (ongoing)
            {
                player.sendMessage(ChatColor.RED + "Un événement est déjà en cours. Terminez le en faisant gagner la/les récompense(s) à un joueur.");
                return true;
            }

            if (args.length == 5)
            {
                String gameCodeName = args[1];
                String map = args[2];
                int rewardsId = Integer.parseInt(args[3]);

                jedis = SamaGamesAPI.get().getBungeeResource();

                if (jedis.exists("hub:event:cooldown:" + player.getUniqueId().toString() + ":" + rewardsId))
                {
                    player.sendMessage("Impossible de créer l'événement : les gains choisis sont indisponibles pour le moment.");
                    jedis.close();

                    return true;
                }

                String template = gameCodeName.equals("hub") ? SamaGamesAPI.get().getServerName().split("_")[1] : this.hub.getGameManager().getGameByIdentifier(gameCodeName).getGameSignsByMap(map).get(0).getTemplate();

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, REWARDS[rewardsId][2]);

                long eventId = -1;

                try
                {
                    Class<?> coreAPIPluginClass = Class.forName("net.samagames.core.APIPlugin");
                    Field gameServiceManagerField = coreAPIPluginClass.getDeclaredField("gameServiceManager");
                    gameServiceManagerField.setAccessible(true);

                    Timestamp now = new Timestamp(new Date().getTime());

                    GameServiceManager gameServiceManager = (GameServiceManager) gameServiceManagerField.get(this.hub.getServer().getPluginManager().getPlugin("SamaGamesAPI"));
                    gameServiceManager.createEvent(new EventBean(0, player.getUniqueId(), template, REWARDS[rewardsId][0], REWARDS[rewardsId][1], now));

                    eventId = gameServiceManager.getEvent(now).getEventId();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                if (eventId == -1)
                {
                    player.sendMessage(ChatColor.RED + "Une erreur s'est produite, veuillez contacter un développeur.");
                    return true;
                }

                jedis.set("hub:event:current:" + player.getUniqueId().toString(), eventId + ":" + gameCodeName + ":" + template + ":" + String.valueOf(rewardsId));
                jedis.set("hub:event:cooldown:" + player.getUniqueId().toString() + ":" + rewardsId, String.valueOf(calendar.getTime().getTime()));
                jedis.expire("hub:event:cooldown:" + player.getUniqueId().toString() + ":" + rewardsId, REWARDS[rewardsId][2]);

                jedis.close();

                player.sendMessage(ChatColor.GREEN + "Votre gain est verouillé. La commande " + ChatColor.GOLD + "/event win <pseudo>" + ChatColor.GREEN + " donnera au joueur les gains sélectionnés.");

                if (!gameCodeName.equals("hub"))
                {
                    this.hub.getEventListener().setWaiting(player.getUniqueId(), this.hub.getGameManager().getGameByIdentifier(gameCodeName).getName());
                    this.hub.getHydroangeasManager().orderServer(player.getName(), template);

                    player.sendMessage("");
                    player.sendMessage(ChatColor.GREEN + "Votre serveur à été commandé. Veut sera téléporté dessus en tant que modérateur. Vous ne pourrez donc pas participer au jeu.");
                }

                this.hub.getServer().getScheduler().runTaskLaterAsynchronously(this.hub, () -> SamaGamesAPI.get().getPubSub().send("eventChannel", gameCodeName + ":" + template + ":" + REWARDS[rewardsId][0] + ":" + REWARDS[rewardsId][1]), 20L);
            }
            else if (args.length == 4)
            {
                String gameCodeName = args[1];
                String map = args[2];
                int rewardsId = Integer.parseInt(args[3]);

                player.sendMessage(ChatUtils.getCenteredText(ChatColor.WHITE + "• Étape 4 : Confirmation •"));
                player.sendMessage("");

                player.sendMessage(ChatColor.WHITE + "- Jeu : " + ChatColor.GRAY + (gameCodeName.equals("hub") ? "Hub" : this.hub.getGameManager().getGameByIdentifier(gameCodeName).getName()));
                player.sendMessage(ChatColor.WHITE + "- Carte : " + ChatColor.GRAY + map);

                int[] prices = REWARDS[rewardsId];
                String pricesLine = "";

                if (prices[0] > 0)
                    pricesLine += ChatColor.GOLD + "" + prices[0] + " pièce" + (prices[0] > 1 ? "s" : "");

                if (prices[0] > 0 && prices[1] > 0)
                    pricesLine += ChatColor.GRAY + " et ";

                if (prices[1] > 0)
                    pricesLine += ChatColor.GREEN + "" + prices[1] + " perle" + (prices[1] > 1 ? "s" : "");

                player.sendMessage(ChatColor.WHITE + "- Gains : " + pricesLine);
                player.sendMessage("");

                new FancyMessage("[Lancer l'événement]").color(ChatColor.GREEN).command("/event create " + gameCodeName + " " + map + " " + rewardsId + " confirm").send(player);
            }
            else if (args.length == 3)
            {
                String gameCodeName = args[1];
                String template = args[2];

                player.sendMessage(ChatUtils.getCenteredText(ChatColor.WHITE + "• Étape 3 : Choix des gains •"));
                player.sendMessage("");

                jedis = SamaGamesAPI.get().getBungeeResource();

                for (int i = 0; i < REWARDS.length; i++)
                    this.showPrices(jedis, player, gameCodeName, template, i, REWARDS[i][0], REWARDS[i][1]);

                jedis.close();
            }
            else if (args.length == 2)
            {
                String gameCodeName = args[1];

                player.sendMessage(ChatUtils.getCenteredText(ChatColor.WHITE + "• Étape 2 : Choix de la carte •"));
                player.sendMessage("");

                if (gameCodeName.equals("hub"))
                {
                    new FancyMessage("[\u25B6]").color(ChatColor.GREEN)
                            .command("/event create " + gameCodeName + " Hub")
                            .tooltip(ChatColor.GOLD + "» Clic pour sélectionner")
                            .then(" Hub").color(ChatColor.GRAY)
                            .send(player);
                }
                else
                {
                    for (String map : this.hub.getGameManager().getGameByIdentifier(gameCodeName).getSigns().keySet())
                    {
                        new FancyMessage("[\u25B6]").color(ChatColor.GREEN)
                                .command("/event create " + gameCodeName + " " + map)
                                .tooltip(ChatColor.GOLD + "» Clic pour sélectionner")
                                .then(" " + map).color(ChatColor.GRAY)
                                .send(player);
                    }
                }
            }
            else
            {
                player.sendMessage(ChatUtils.getCenteredText(ChatColor.WHITE + "• Étape 1 : Choix du jeu •"));
                player.sendMessage("");

                new FancyMessage("[\u25B6]").color(ChatColor.GREEN)
                        .command("/event create hub")
                        .tooltip(ChatColor.GOLD + "» Clic pour sélectionner")
                        .then(" Hub (Votre serveur actuel)").color(ChatColor.GRAY)
                        .send(player);

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

            if(!jedis.exists("hub:event:current:" + player.getUniqueId().toString()))
            {
                player.sendMessage(ChatColor.RED + "Vous n'avez aucun événement en cours.");
                jedis.close();

                return true;
            }

            String[] eventData = jedis.get("hub:event:current:" + player.getUniqueId().toString()).split(":");

            int eventId = Integer.parseInt(eventData[0]);
            int rewardsId = Integer.parseInt(eventData[3]);

            if (REWARDS[rewardsId][0] > 0)
                SamaGamesAPI.get().getPlayerManager().getPlayerData(this.hub.getServer().getPlayer(playerName).getUniqueId()).creditCoins(REWARDS[rewardsId][0], "Evénement", false);

            if (REWARDS[rewardsId][1] > 0)
            {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, 7);

                for (int i = 0; i < REWARDS[rewardsId][1]; i++)
                    this.hub.getInteractionManager().getWellManager().addPearlToPlayer(this.hub.getServer().getPlayer(playerName), new Pearl(UUID.randomUUID(), new Random().nextInt(4) + 1, calendar.getTime().getTime()));

                this.hub.getInteractionManager().getGraouManager().update(this.hub.getServer().getPlayer(playerName));
                this.hub.getServer().getPlayer(playerName).sendMessage(ChatColor.GREEN + "+" + REWARDS[rewardsId][1] + " perle" + (REWARDS[rewardsId][1] > 1 ? "s" : "") + " (Evénement)");
            }

            try
            {
                Class<?> coreAPIPluginClass = Class.forName("net.samagames.core.APIPlugin");
                Field gameServiceManagerField = coreAPIPluginClass.getDeclaredField("gameServiceManager");
                gameServiceManagerField.setAccessible(true);

                GameServiceManager gameServiceManager = (GameServiceManager) gameServiceManagerField.get(this.hub.getServer().getPluginManager().getPlugin("SamaGamesAPI"));
                gameServiceManager.createEventWinner(new EventWinnerBean(0, eventId, this.hub.getServer().getPlayer(playerName).getUniqueId()));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            this.hub.getScoreboardManager().update(this.hub.getServer().getPlayer(playerName));

            new FancyMessage("Le joueur a bien été crédité de ses gains. Cliquez ").color(ChatColor.GREEN)
                    .then("[ICI]").color(ChatColor.AQUA).style(ChatColor.BOLD).command("/event end")
                    .then(" pour marquer l'événement comme terminé. Sinon, vous pouvez continuer à distribuer cette même récompense, en tenant compte du temps de rechargement de celle-ci.").color(ChatColor.GREEN).send(player);

        }
        else if (subCommand.equals("remind"))
        {
            Jedis jedis = SamaGamesAPI.get().getBungeeResource();

            if (!jedis.exists("hub:event:current:" + player.getUniqueId().toString()))
            {
                player.sendMessage(ChatColor.RED + "Il n'y a aucun événement en cours.");
                jedis.close();

                return true;
            }

            String[] eventData = jedis.get("hub:event:current:" + player.getUniqueId().toString()).split(":");

            jedis.close();

            int eventId = Integer.parseInt(eventData[0]);
            String gameCodeName = eventData[1];
            String map = eventData[2];
            int rewardsId = Integer.parseInt(eventData[3]);

            if (!gameCodeName.equals("hub"))
            {
                player.sendMessage(ChatColor.RED + "Vous ne pouvez utiliser cette commande uniquement avec les événements relatifs aux Hubs.");
                return true;
            }

            SamaGamesAPI.get().getPubSub().send("eventChannel", gameCodeName + ":" + map + ":" + REWARDS[rewardsId][0] + ":" + REWARDS[rewardsId][1]);
        }
        else if (subCommand.equals("end"))
        {
            Jedis jedis = SamaGamesAPI.get().getBungeeResource();

            if (!jedis.exists("hub:event:current:" + player.getUniqueId().toString()))
            {
                player.sendMessage(ChatColor.RED + "Il n'y a aucun événement en cours.");
                jedis.close();

                return true;
            }

            jedis.del("hub:event:current:" + player.getUniqueId().toString());
            jedis.close();

            player.sendMessage(ChatColor.GREEN + "L'événement est maintenant marqué comme terminé.");
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
        this.showHelpSubCommand(player, "win <pseudo>", "Déclarer gagnant un joueur et lui donner sa récompense");
        this.showHelpSubCommand(player, "remind", "Refaire l'annonce d'un événement en cours");
        this.showHelpSubCommand(player, "end", "Terminer l'événement en cours. A utiliser en cas de grande nécéssité");

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
