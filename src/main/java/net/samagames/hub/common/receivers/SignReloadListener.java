package net.samagames.hub.common.receivers;

import net.samagames.api.pubsub.IPacketsReceiver;
import net.samagames.hub.Hub;

public class SignReloadListener implements IPacketsReceiver
{
    private final Hub hub;

    public SignReloadListener(Hub hub)
    {
        this.hub = hub;
    }

    @Override
    public void receive(String channel, String packet)
    {
        this.hub.getSignManager().reloadList();
    }
}
