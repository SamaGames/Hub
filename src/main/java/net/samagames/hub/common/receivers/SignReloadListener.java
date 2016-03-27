package net.samagames.hub.common.receivers;

import net.samagames.api.pubsub.IPacketsReceiver;
import net.samagames.hub.Hub;

public class SignReloadListener implements IPacketsReceiver
{
    public SignReloadListener(Hub hub)
    {

    }

    @Override
    public void receive(String channel, String packet)
    {
        //this.signManager.reloadList();
    }
}
