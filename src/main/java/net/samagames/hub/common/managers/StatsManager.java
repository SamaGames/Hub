package net.samagames.hub.common.managers;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.stats.Leaderboard;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.DisplayedStat;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.logging.Level;

public class StatsManager extends AbstractManager
{
    private final HashMap<String, Leaderboard> leaderboards;

    public StatsManager(Hub hub)
    {
        super(hub);
        this.leaderboards = new HashMap<>();

        Bukkit.getScheduler().runTaskTimerAsynchronously(this.hub, this::reloadStats, 0L, 20L * 60 * 3);
    }

    public void reloadStats()
    {
        this.hub.log(this, Level.INFO, "Reloading leaderboards...");

        for(AbstractGame game : this.hub.getGameManager().getGames().values())
        {
            if(game.getDisplayedStats() == null)
                continue;

            for(DisplayedStat stat : game.getDisplayedStats())
            {
                if(this.hub.isDebugEnabled())
                    this.hub.log(this, Level.INFO, "Reloading stat '" + stat.getDatabaseName() + "' for the game " + game.getCodeName());

                if(this.leaderboards.containsKey(game.getCodeName() + "_" + stat.getDatabaseName()))
                    this.leaderboards.remove(game.getCodeName() + "_" + stat.getDatabaseName());

                try
                {
                    Leaderboard leaderboard = SamaGamesAPI.get().getStatsManager(game.getCodeName()).getLeaderboard(stat.getDatabaseName());
                    this.leaderboards.put(game.getCodeName() + "_" + stat.getDatabaseName(), leaderboard);
                }
                catch (Exception e)
                {
                    this.hub.log(this, Level.SEVERE, "Failed to reload the stat '" + stat.getDatabaseName() + "' for the game " + game.getCodeName());
                }
            }
        }

        this.hub.log(this, Level.INFO, "Leaderboards reloaded!");
    }

    public Leaderboard getLeaderbordOf(String game, String stat)
    {
        if(this.leaderboards.containsKey(game + "_" + stat))
            return this.leaderboards.get(game + "_" + stat);
        else
            return null;
    }

    @Override
    public String getName()
    {
        return "StatsManager";
    }
}
