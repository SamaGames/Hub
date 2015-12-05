package net.samagames.hub.common.managers;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.player.AbstractPlayerData;
import net.samagames.hub.Hub;
import net.samagames.hub.utils.RankUtils;
import net.samagames.tools.Rainbow;
import net.samagames.tools.scoreboards.ObjectiveSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class ScoreboardManager extends AbstractManager
{
    private final Map<UUID, ObjectiveSign> playerObjectives;
    private final ArrayList<ChatColor> rainbowContent;
    private int rainbowIndex;
    private ScheduledFuture<?> refreshTask;

    public ScoreboardManager(Hub hub)
    {
        super(hub);

        this.playerObjectives = new ConcurrentHashMap<>();
        this.rainbowContent = Rainbow.getRainbow();
        this.rainbowIndex = 0;

        refreshTask = hub.getScheduledExecutorService().scheduleAtFixedRate(this::update, 1, 1, TimeUnit.SECONDS);
    }

    public void addScoreboardReceiver(Player player)
    {
        if(!this.playerObjectives.containsKey(player.getUniqueId()))
        {
            AbstractPlayerData data = SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId());
            ObjectiveSign objective = new ObjectiveSign("ixeDiDiDi" + SamaGamesAPI.get().getServerName(), "SamaGames");

            objective.setDisplayName(this.rainbowContent.get(this.rainbowIndex) + "✦" + ChatColor.BOLD + " SamaGames " + ChatColor.RESET + this.rainbowContent.get(this.rainbowIndex) + "✦");
            objective.setLine(0, ChatColor.BLUE + "");
            objective.setLine(1, ChatColor.GREEN + "" + ChatColor.BOLD + "Serveur");
            objective.setLine(2, ChatColor.GRAY + "Hub " + SamaGamesAPI.get().getServerName().split("_")[1]);
            objective.setLine(3, ChatColor.AQUA + "");
            objective.setLine(4, ChatColor.RED + "" + ChatColor.BOLD + "Rang");
            objective.setLine(5, RankUtils.getFormattedRank(player.getUniqueId()));
            objective.setLine(6, ChatColor.GREEN + "");
            objective.setLine(7, ChatColor.GOLD + "" + ChatColor.BOLD + "Pièces");
            objective.setLine(8, ChatColor.GRAY + (data == null ? "Erreur" : String.valueOf(data.getCoins())));
            objective.setLine(9, ChatColor.DARK_GREEN + "");
            objective.setLine(10, ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Étoiles");
            objective.setLine(11, ChatColor.RESET + "" + ChatColor.GRAY + (data == null ? "Erreur" : String.valueOf(data.getStars())));
            objective.setLine(12, ChatColor.BLACK + "");
            objective.setLine(13, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "TeamSpeak");
            objective.setLine(14, ChatColor.GRAY + "ts.samagames.net");
            objective.addReceiver(player);

            this.playerObjectives.put(player.getUniqueId(), objective);

            if(Hub.getInstance().isDebugEnabled())
                Hub.getInstance().log(this, Level.INFO, "Added scoreboard receiver (" + player.getUniqueId() + ")");

            //this.update(player.getUniqueId());
        }
    }

    public void removeScoreboardReceiver(Player player)
    {
        if(this.playerObjectives.containsKey(player.getUniqueId()))
        {
            this.playerObjectives.get(player.getUniqueId()).removeReceiver(player);
            this.playerObjectives.remove(player.getUniqueId());

            if(Hub.getInstance().isDebugEnabled())
                Hub.getInstance().log(this, Level.INFO, "Removed scoreboard receiver (" + player.getUniqueId() + ")");
        }
    }

    public void update()
    {
        ArrayList<UUID> uuids = new ArrayList<>();
        uuids.addAll(this.playerObjectives.keySet());
        uuids.forEach(this::update);

        this.rainbowIndex++;

        if(this.rainbowIndex == this.rainbowContent.size())
            this.rainbowIndex = 0;
    }

    private void update(UUID uuid)
    {
        update(uuid, false);
    }

    public void update(UUID uuid, boolean refresh)
    {
        ObjectiveSign objective = this.playerObjectives.get(uuid);

        ChatColor rainbow = this.rainbowContent.get(this.rainbowIndex);
        objective.setDisplayName(rainbow + "✦" + ChatColor.BOLD + " SamaGames " + ChatColor.RESET + rainbow + "✦");

        if (refresh)
        {
            AbstractPlayerData data = SamaGamesAPI.get().getPlayerManager().getPlayerData(uuid);
            objective.setLine(8, ChatColor.GRAY + (data == null ? "Erreur" : String.valueOf(data.getCoins())));
            objective.setLine(11, ChatColor.RESET + "" + ChatColor.GRAY + (data == null ? "Erreur" : String.valueOf(data.getStars())));
        }
        objective.updateLines();
    }

    @Override
    public void onServerClose()
    {
        this.refreshTask.cancel(true);
        for(UUID uuid : this.playerObjectives.keySet())
        {
            this.playerObjectives.get(uuid).removeReceiver(Bukkit.getOfflinePlayer(uuid));
        }

        this.playerObjectives.clear();
    }

    @Override
    public String getName() { return "ScoreboardManager"; }
}
