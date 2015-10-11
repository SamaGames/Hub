package net.samagames.hub.games.sign;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class GameSign
{
    private final Sign sign;
    private final AbstractGame game;
    private final String map;
    private final ChatColor color;
    private final String template;
    private final BukkitTask updateTask;

    private final BukkitTask scrollTask;

    private int scrollIndex = 0;
    private String scrolledMapName;

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

        this.scrollTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Hub.getInstance(), this::scrollMapName, 20L, 12L);

        this.updateTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Hub.getInstance(), this::update, 20L, 20L);
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

        String mapLine = this.color + "» " + ChatColor.BOLD + this.scrolledMapName + ChatColor.RESET + this.color + " «";

        this.sign.setLine(0, this.game.getName());
        this.sign.setLine(1, mapLine);
        this.sign.setLine(2, playerWaitFor + "" + ChatColor.RESET + " en attente");
        this.sign.setLine(3, totalPlayerOnServers + "" + ChatColor.RESET + " en jeu");

        Bukkit.getScheduler().runTask(Hub.getInstance(), this.sign::update);
    }

    public void updateMapName()
    {
        this.sign.setLine(1, this.color + "» " + ChatColor.BOLD + this.scrolledMapName + ChatColor.RESET + this.color + " «");
        Bukkit.getScheduler().runTask(Hub.getInstance(), this.sign::update);
    }

    public void scrollMapName()
    {
        if(map.length() <= 10)
            return;

        int start = scrollIndex;
        int end = scrollIndex+10;

        if(end > map.length())
        {
            scrollIndex=0;
            return;
        }

        scrolledMapName = map.substring(start, end);
        scrollIndex++;
        updateMapName();
    }

    public void click(Player player)
    {
        if(this.game.isMaintenance())
        {
            player.sendMessage(ChatColor.RED + "Ce jeu est actuellement en maintenance.");
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

    public String getTemplate()
    {
        return template;
    }

    public String getMap()
    {
        return map;
    }

    public ChatColor getColor()
    {
        return color;
    }

    public void setPlayerWaitFor(int playerWaitFor)
    {
        this.playerWaitFor = playerWaitFor;
    }

    public void setTotalPlayerOnServers(int totalPlayerOnServers)
    {
        this.totalPlayerOnServers = totalPlayerOnServers;
    }

    public void onDelete()
    {
        updateTask.cancel();
    }
}
