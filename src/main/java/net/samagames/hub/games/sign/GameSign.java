package net.samagames.hub.games.sign;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.ServerStatus;
import net.samagames.api.games.Status;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import redis.clients.jedis.Jedis;

import java.util.LinkedHashMap;

public class GameSign
{
    private final Sign sign;
    private final AbstractGame game;
    private final String map;

    private LinkedHashMap<String, ServerStatus> lastDatas;

    public GameSign(AbstractGame game, String map, Sign sign)
    {
        this.sign = sign;
        this.game = game;
        this.map = map;

        this.lastDatas = new LinkedHashMap<>();

        this.sign.setMetadata("game", new FixedMetadataValue(Hub.getInstance(), game.getCodeName()));
        this.sign.setMetadata("map", new FixedMetadataValue(Hub.getInstance(), map));

        this.update(null);
    }

    public void update(ServerStatus data)
    {
        if(this.game.isMaintenance())
        {
            this.sign.setLine(0, "");
            this.sign.setLine(1, ChatColor.DARK_RED + "Jeu en");
            this.sign.setLine(2, ChatColor.DARK_RED + "maintenance !");
            this.sign.setLine(3, "");

            Bukkit.getScheduler().runTask(Hub.getInstance(), this.sign::update);
            return;
        }

        String mapLine = ChatColor.DARK_RED + "× " + ChatColor.BOLD + this.map + ChatColor.RESET + ChatColor.DARK_RED + " ×";

        if(data != null)
        {
            if(data.getStatus() == Status.WAITING_FOR_PLAYERS || data.getStatus() == Status.READY_TO_START || data.getStatus() == Status.IN_GAME)
                this.lastDatas.put(data.getBungeeName(), data);
            else
                this.lastDatas.remove(data.getBungeeName());

            if(!this.lastDatas.isEmpty())
                mapLine = ChatColor.GREEN + "» " + ChatColor.BOLD + this.map + ChatColor.RESET + ChatColor.GREEN + " «";
        }

        this.sign.setLine(0, this.game.getName());
        this.sign.setLine(1, mapLine);
        this.sign.setLine(2, this.getWaitingPlayers() + "" + ChatColor.RESET + " en attente");
        this.sign.setLine(3, this.getPlayingPlayers() + "" + ChatColor.RESET + " en jeu");

        Bukkit.getScheduler().runTask(Hub.getInstance(), this.sign::update);
    }

    public void click(Player player)
    {
        Jedis jedis = SamaGamesAPI.get().getResource();

        String ban = jedis.get("gamebanlist:reason:" + player.getUniqueId());

        if (ban != null)
        {
            long ttl = jedis.ttl("gamebanlist:reason:" + player.getUniqueId());
            String duration = "définitivement";

            if (ttl >= 0)
                duration = TimeUtils.formatTime(ttl);

            player.sendMessage(ChatColor.RED + "Vous êtes banni du jeu " + duration + ".");
            player.sendMessage(ChatColor.RED + "Motif : " + ban);

            jedis.close();

            return;
        }

        if(this.lastDatas == null)
        {
            return;
        }
        else if(this.lastDatas.isEmpty())
        {
            player.sendMessage(ChatColor.RED + "Aucun serveur n'est prêt à vous reçevoir actuellement.");
            return;
        }

        ServerStatus firstServer = this.lastDatas.values().iterator().next();
        String[] serverNameParts = firstServer.getBungeeName().split("_");

        player.sendMessage(ChatColor.GREEN + "Vous avez été envoyé vers le serveur " + serverNameParts[0] + " " + serverNameParts[1] + " !");
        SamaGamesAPI.get().getProxyDataManager().getProxiedPlayer(player.getUniqueId()).connectGame(firstServer.getBungeeName());
    }

    public void developperClick(Player player)
    {
        player.sendMessage(ChatColor.GOLD + "----------------------------------------");
        player.sendMessage(ChatColor.GOLD + "Informations du panneau de jeu :");
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Jeu : " + ChatColor.GREEN + this.game.getCodeName());
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Map : " + ChatColor.GREEN + this.map);
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Joueurs en attente : " + ChatColor.GREEN + this.getWaitingPlayers());
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Joueurs en jeu : " + ChatColor.GREEN + this.getPlayingPlayers());
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Serveurs en ligne : " + ChatColor.GREEN + this.getWaitingServers() + " en attente et " + this.getPlayingServers() + " en jeu");
        player.sendMessage(ChatColor.GOLD + "----------------------------------------");
    }

    public int getWaitingPlayers()
    {
        int temp = 0;

        for(ServerStatus status : this.lastDatas.values())
            if(status.getStatus() == Status.WAITING_FOR_PLAYERS || status.getStatus() == Status.READY_TO_START)
                temp += status.getPlayers();

        return temp;
    }

    public int getPlayingPlayers()
    {
        int temp = 0;

        for(ServerStatus status : this.lastDatas.values())
            if(status.getStatus() == Status.IN_GAME)
                temp += status.getPlayers();

        return temp;
    }

    public int getWaitingServers()
    {
        int temp = 0;

        for(ServerStatus status : this.lastDatas.values())
            if(status.getStatus() == Status.WAITING_FOR_PLAYERS || status.getStatus() == Status.READY_TO_START)
                temp += 1;

        return temp;
    }

    public int getPlayingServers()
    {
        int temp = 0;

        for(ServerStatus status : this.lastDatas.values())
            if(status.getStatus() == Status.IN_GAME)
                temp += 1;

        return temp;
    }

    public Sign getSign()
    {
        return this.sign;
    }
}
