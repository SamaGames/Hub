package net.samagames.hub.common.managers;

import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteractionManager;

import java.util.logging.Level;

public abstract class AbstractManager implements EntryPoints
{
    protected final Hub hub;

    public AbstractManager(Hub hub)
    {
        this.hub = hub;

        if (!(this instanceof AbstractInteractionManager))
            this.hub.getEventBus().registerManager(this);
    }

    public void log(Level level, String message)
    {
        this.hub.getLogger().log(level, "[" + this.getClass().getSimpleName() + "] " + message);
    }
}
