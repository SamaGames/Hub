package net.samagames.hub.common.tasks;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*
 * This file is part of Hub.
 *
 * Hub is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hub is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hub.  If not, see <http://www.gnu.org/licenses/>.
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
