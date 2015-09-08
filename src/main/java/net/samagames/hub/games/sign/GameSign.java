package net.samagames.hub.games.sign;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import redis.clients.jedis.Jedis;

import java.util.UUID;

public class GameSign
{
    private final Sign sign;
    private final AbstractGame game;
    private final String map;
    private final ChatColor color;
    private final String template;

    private int playerMaxForMap;
    private int playerWaitFor;
    private int totalPlayerOnServers;

    public GameSign(AbstractGame game, String map, ChatColor color, String template, Sign sign)
    {
        this.sign = sign;
        this.game = game;
        this.map = map;
        this.color = color;
        this.template = template;

        this.sign.setMetadata("game", new FixedMetadataValue(Hub.getInstance(), game.getCodeName()));
        this.sign.setMetadata("map", new FixedMetadataValue(Hub.getInstance(), map));

        Bukkit.getScheduler().runTaskTimerAsynchronously(Hub.getInstance(), this::update, 20L, 20L);
    }

    public void update()
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

        String mapLine = this.color + "» " + ChatColor.BOLD + this.map + ChatColor.RESET + this.color + " «";

        this.sign.setLine(0, this.game.getName());
        this.sign.setLine(1, mapLine);
        this.sign.setLine(2, playerWaitFor + "" + ChatColor.RESET + " en attente");
        this.sign.setLine(3, playerMaxForMap + "" + ChatColor.RESET + " par map");

        Bukkit.getScheduler().runTask(Hub.getInstance(), this.sign::update);
    }

    public void click(Player player)
    {

        //TODO: check if we keep it
        Jedis jedis = SamaGamesAPI.get().getBungeeResource();

        String ban = jedis.get("gamebanlist:" + this.game.getCodeName() + ":reason:" + player.getUniqueId());

        if (ban != null)
        {
            long ttl = jedis.ttl("gamebanlist:" + this.game.getCodeName() + ":reason:" + player.getUniqueId());
            String duration = "définitivement";

            if (ttl >= 0)
                duration = TimeUtils.formatTime(ttl);

            player.sendMessage(ChatColor.RED + "Vous êtes banni du jeu " + duration + ".");
            player.sendMessage(ChatColor.RED + "Motif : " + ban);

            jedis.close();

            return;
        }

        UUID partyUUID = SamaGamesAPI.get().getPartiesManager().getPlayerParty(player.getUniqueId());

        if(partyUUID == null)
        {
            Hub.getInstance().getHydroManager().addPlayerToQueue(player.getUniqueId(), this.template);
        }
        else
        {
            if(!SamaGamesAPI.get().getPartiesManager().getLeader(partyUUID).equals(player.getUniqueId()))
            {
                player.sendMessage(ChatColor.RED + "Vous n'êtes pas le leader, vous ne pouvez pas ajouter votre partie dans une queue.");
                return;
            }

            Hub.getInstance().getHydroManager().addPartyToQueue(player.getUniqueId(), partyUUID, this.template);
        }
    }

    public void developperClick(Player player)
    {
        player.sendMessage(ChatColor.GOLD + "----------------------------------------");
        player.sendMessage(ChatColor.GOLD + "Informations du panneau de jeu :");
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Jeu : " + ChatColor.GREEN + this.game.getCodeName());
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Map : " + ChatColor.GREEN + this.map);
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Joueurs en attente : " + ChatColor.GREEN + playerWaitFor);
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Joueurs en jeu : " + ChatColor.GREEN + totalPlayerOnServers);
        //player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Serveurs en ligne : " + ChatColor.GREEN + this.getWaitingServers() + " en attente et " + this.getPlayingServers() + " en jeu");
        player.sendMessage(ChatColor.GOLD + "----------------------------------------");
    }

    public String getTemplate() {
        return template;
    }

    public String getMap() {
        return map;
    }

    public ChatColor getColor() {
        return color;
    }

    public Sign getSign()
    {
        return this.sign;
    }

    public int getPlayerMaxForMap() {
        return playerMaxForMap;
    }

    public void setPlayerMaxForMap(int playerMaxForMap) {
        this.playerMaxForMap = playerMaxForMap;
    }

    public int getPlayerWaitFor() {
        return playerWaitFor;
    }

    public void setPlayerWaitFor(int playerWaitFor) {
        this.playerWaitFor = playerWaitFor;
    }

    public int getTotalPlayerOnServers() {
        return totalPlayerOnServers;
    }

    public void setTotalPlayerOnServers(int totalPlayerOnServers) {
        this.totalPlayerOnServers = totalPlayerOnServers;
    }
}
