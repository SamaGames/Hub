package net.samagames.hub.common.receivers;

import net.samagames.api.pubsub.IPacketsReceiver;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.sign.GameSign;

public class MaintenanceListener implements IPacketsReceiver
{
    @Override
    public void receive(String channel, String packet)
    {
        String[] data = packet.split(":");

        String game = data[0];
        String template = data[1];
        boolean flag = Boolean.valueOf(data[2]);

        AbstractGame gameObject = Hub.getInstance().getGameManager().getGameByIdentifier(game);

        if(gameObject == null)
            return;

        GameSign sign = gameObject.getGameSignByTemplate(template);

        if(sign == null)
            return;

        sign.setMaintenance(flag);
    }
}
