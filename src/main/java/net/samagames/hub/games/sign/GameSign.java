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

    public GameSign(AbstractGame game, String map, Sign sign)
    {
        this.sign = sign;
        this.game = game;
        this.map = map;

        this.sign.setMetadata("game", new FixedMetadataValue(Hub.getInstance(), game.getCodeName()));
        this.sign.setMetadata("map", new FixedMetadataValue(Hub.getInstance(), map));

        this.update();
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

        String mapLine = ChatColor.GREEN + "» " + ChatColor.BOLD + this.map + ChatColor.RESET + ChatColor.GREEN + " «";

        this.sign.setLine(0, this.game.getName());
        this.sign.setLine(1, mapLine);
        this.sign.setLine(2, 0 + "" + ChatColor.RESET + " en attente");
        this.sign.setLine(3, 0 + "" + ChatColor.RESET + " en jeu");

        Bukkit.getScheduler().runTask(Hub.getInstance(), this.sign::update);
    }

    public void click(Player player)
    {
        Jedis jedis = SamaGamesAPI.get().getResource();

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
            Hub.getInstance().getHydroManager().addPlayerToQueue(player.getUniqueId(), game.getName(), map);
        }
        else
        {
            if(!SamaGamesAPI.get().getPartiesManager().getLeader(partyUUID).equals(player.getUniqueId()))
            {
                player.sendMessage(ChatColor.RED + "Vous n'êtes pas le leader, vous ne pouvez pas ajouter votre partie dans une queue.");
                return;
            }

            Hub.getInstance().getHydroManager().addPartyToQueue(player.getUniqueId(), partyUUID, game.getName(), map);
        }
    }

    public void developperClick(Player player)
    {
        player.sendMessage(ChatColor.GOLD + "----------------------------------------");
        player.sendMessage(ChatColor.GOLD + "Informations du panneau de jeu :");
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Jeu : " + ChatColor.GREEN + this.game.getCodeName());
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Map : " + ChatColor.GREEN + this.map);
        //player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Joueurs en attente : " + ChatColor.GREEN + this.getWaitingPlayers());
        //player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Joueurs en jeu : " + ChatColor.GREEN + this.getPlayingPlayers());
        //player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Serveurs en ligne : " + ChatColor.GREEN + this.getWaitingServers() + " en attente et " + this.getPlayingServers() + " en jeu");
        player.sendMessage(ChatColor.GOLD + "----------------------------------------");
    }

    public Sign getSign()
    {
        return this.sign;
    }
}
