package net.samagames.hub.common.managers;

import net.samagames.hub.Hub;

import java.util.logging.Level;

public abstract class AbstractManager implements EntryPoints
{
    protected final Hub hub;

    public AbstractManager(Hub hub)
    {
        this.hub = hub;
        this.hub.getEventBus().registerManager(this);
    }

    protected void log(Level level, String message)
    {
        this.hub.getLogger().log(level, "[" + this.getClass().getSimpleName() + "] " + message);
    }
}
