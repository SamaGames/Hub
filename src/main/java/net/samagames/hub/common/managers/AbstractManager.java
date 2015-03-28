package net.samagames.hub.common.managers;

import net.samagames.hub.Hub;

public abstract class AbstractManager
{
    protected final Hub hub;

    public AbstractManager(Hub hub)
    {
        this.hub = hub;
    }

    public void onServerClose() {}

    public abstract String getName();
}
