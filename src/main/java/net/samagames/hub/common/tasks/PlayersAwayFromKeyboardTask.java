package net.samagames.hub.common.tasks;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.tools.chat.ActionBarAPI;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 21/10/2016
 */
public class PlayersAwayFromKeyboardTask extends AbstractTask
{
    private final Map<UUID, Integer> timers;

    PlayersAwayFromKeyboardTask(Hub hub)
    {
        super(hub);

        this.timers = new ConcurrentHashMap<>();
        this.task = this.hub.getServer().getScheduler().runTaskTimerAsynchronously(hub, this, 20L, 20L);
    }

    @Override
    public void run()
    {
        for (UUID uuid : this.timers.keySet())
        {
            if (this.hub.getServer().getPlayer(uuid) == null)
            {
                this.removePlayer(uuid);
                continue;
            }

            int timer = this.timers.get(uuid);
            timer++;

            if (timer == 72000) // 1 hour
            {
                SamaGamesAPI.get().getAchievementManager().getAchievementByID(53).unlock(uuid);
                this.removePlayer(uuid);
            }
            else
            {
                ActionBarAPI.sendPermanentMessage(this.hub.getServer().getPlayer(uuid), String.valueOf(timer));
                this.timers.put(uuid, timer);
            }
        }
    }

    public void registerPlayer(UUID uuid)
    {
        this.timers.put(uuid, 0);
    }

    public void removePlayer(UUID uuid)
    {
        if (this.timers.containsKey(uuid))
            this.timers.remove(uuid);
    }

    public void resetPlayer(UUID uuid)
    {
        if (this.timers.containsKey(uuid))
            this.timers.put(uuid, 0);
    }
}
