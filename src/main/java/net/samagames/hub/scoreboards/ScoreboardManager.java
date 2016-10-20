package net.samagames.hub.scoreboards;

import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class ScoreboardManager extends AbstractManager
{
    private final Map<UUID, PersonalScoreboard> scoreboards;
    private final ScheduledFuture glowingTask;
    private final ScheduledFuture reloadingTask;

    public ScoreboardManager(Hub hub)
    {
        super(hub);

        this.scoreboards = new HashMap<>();

        this.glowingTask = this.hub.getScheduledExecutorService().scheduleAtFixedRate(() ->
        {
            for (PersonalScoreboard scoreboard : this.scoreboards.values())
                this.hub.getExecutorMonoThread().execute(scoreboard::setLines);
        }, 200, 200, TimeUnit.MILLISECONDS);

        this.reloadingTask = this.hub.getScheduledExecutorService().scheduleAtFixedRate(() ->
        {
            for (PersonalScoreboard scoreboard : this.scoreboards.values())
                this.hub.getExecutorMonoThread().execute(scoreboard::reloadData);
        }, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public void onDisable()
    {
        this.glowingTask.cancel(true);
        this.reloadingTask.cancel(true);

        this.scoreboards.values().forEach(PersonalScoreboard::onLogout);
    }

    @Override
    public void onLogin(Player player)
    {
        if (this.scoreboards.containsKey(player.getUniqueId()))
        {
            this.log(Level.WARNING, "The player '" + player.getUniqueId().toString() + "' already have a scoreboard!");
            return;
        }

        this.scoreboards.put(player.getUniqueId(), new PersonalScoreboard(this.hub, player));
        this.log(Level.INFO, "Added scoreboard to '" + player.getUniqueId() + "'.");
    }

    @Override
    public void onLogout(Player player)
    {
        if (this.scoreboards.containsKey(player.getUniqueId()))
        {
            this.scoreboards.get(player.getUniqueId()).onLogout();
            this.scoreboards.remove(player.getUniqueId());

            this.log(Level.INFO, "Removed scoreboard to '" + player.getUniqueId() + "'.");
        }
    }

    public void update(Player player)
    {
        if (this.scoreboards.containsKey(player.getUniqueId()))
            this.scoreboards.get(player.getUniqueId()).reloadData();
    }
}
