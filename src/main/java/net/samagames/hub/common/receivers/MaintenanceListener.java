package net.samagames.hub.common.receivers;

import net.samagames.api.pubsub.IPacketsReceiver;
import net.samagames.hub.Hub;

public class MaintenanceListener implements IPacketsReceiver
{
    @Override
    public void receive(String channel, String packet)
    {
        String[] data = packet.split(":");

        String game = data[0];
        String template = data[1];
        boolean flag = Boolean.valueOf(data[2]);

        Hub.getInstance().getGameManager().getGameByIdentifier(game).getGameSignByTemplate(template).setMaintenance(flag);
    }
}
