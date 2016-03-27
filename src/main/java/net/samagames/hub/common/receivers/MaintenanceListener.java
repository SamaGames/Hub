package net.samagames.hub.common.receivers;

import net.samagames.api.pubsub.IPacketsReceiver;
import net.samagames.hub.Hub;

public class MaintenanceListener implements IPacketsReceiver
{
    public MaintenanceListener(Hub hub)
    {

    }

    @Override
    public void receive(String channel, String packet)
    {
        String[] data = packet.split(":");

        String game = data[0];
        String template = data[1];
        boolean flag = Boolean.parseBoolean(data[2]);

        /**AbstractGame gameObject = this.hub.getInstance().getGameManager().getGameByIdentifier(game);

        if(gameObject == null)
            return;

        GameSign sign = gameObject.getGameSignByTemplate(template);

        if(sign == null)
            return;

        sign.setMaintenance(flag);**/

        // TODO
    }
}
