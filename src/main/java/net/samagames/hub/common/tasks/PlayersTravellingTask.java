package net.samagames.hub.common.tasks;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.tools.chat.ActionBarAPI;
import org.bukkit.Statistic;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 21/10/2016
 */
public class PlayersTravellingTask extends AbstractTask
{
    private final List<UUID> players;

    PlayersTravellingTask(Hub hub)
    {
        super(hub);

        this.players = new CopyOnWriteArrayList<>();
        this.task = this.hub.getServer().getScheduler().runTaskTimerAsynchronously(hub, this, 20L * 5, 20L * 5);
    }

    @Override
    public void run()
    {
        for (UUID uuid : this.players)
        {
            if (this.hub.getServer().getPlayer(uuid) == null)
            {
                this.removePlayer(uuid);
                continue;
            }

            int blocksTravelled = this.hub.getServer().getPlayer(uuid).getStatistic(Statistic.WALK_ONE_CM) / 100;
            this.hub.getServer().getPlayer(uuid).setStatistic(Statistic.WALK_ONE_CM, 0);

            if (blocksTravelled > 0)
            {
                SamaGamesAPI.get().getAchievementManager().incrementAchievement(uuid, 54, blocksTravelled);

                if (SamaGamesAPI.get().getAchievementManager().isUnlocked(uuid, 54))
                    this.removePlayer(uuid);
            }
        }
    }

    public void registerPlayer(UUID uuid)
    {
        this.players.add(uuid);
        this.hub.getServer().getPlayer(uuid).setStatistic(Statistic.WALK_ONE_CM, 0);
    }

    public void removePlayer(UUID uuid)
    {
        if (this.players.contains(uuid))
            this.players.remove(uuid);
    }
}
