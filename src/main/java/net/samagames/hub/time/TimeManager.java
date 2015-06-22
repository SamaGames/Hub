package net.samagames.hub.time;

import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class TimeManager extends AbstractManager
{
    public TimeManager(Hub hub)
    {
        super(hub);

        hub.log(this, Level.INFO, "Starting time synchronization...");
        Bukkit.getScheduler().runTaskTimer(hub, new UpdateTimeTask(), 1L, 6L);
    }

    @Override
    public String getName()
    {
        return "TimeManager";
    }
}
