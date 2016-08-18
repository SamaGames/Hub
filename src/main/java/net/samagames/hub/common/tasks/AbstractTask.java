package net.samagames.hub.common.tasks;

import net.samagames.hub.Hub;
import org.bukkit.scheduler.BukkitTask;

abstract class AbstractTask implements Runnable
{
    protected final Hub hub;
    protected BukkitTask task;

    AbstractTask(Hub hub)
    {
        this.hub = hub;
    }

    public void cancel()
    {
        this.task.cancel();
    }
}
