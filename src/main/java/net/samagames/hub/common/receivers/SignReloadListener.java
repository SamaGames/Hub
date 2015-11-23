package net.samagames.hub.common.receivers;

import net.samagames.api.pubsub.IPacketsReceiver;
import net.samagames.hub.Hub;

public class SignReloadListener implements IPacketsReceiver
{
    @Override
    public void receive(String channel, String packet)
    {
        Hub.getInstance().getSignManager().reloadList();
    }
}
