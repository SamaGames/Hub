package net.samagames.hub.common.receivers;

import net.samagames.api.pubsub.IPacketsReceiver;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.signs.GameSign;

import java.util.List;

public class SoonListener implements IPacketsReceiver
{
    private final Hub hub;

    public SoonListener(Hub hub)
    {
        this.hub = hub;
    }

    @Override
    public void receive(String channel, String packet)
    {
        String[] data = packet.split(":");

        String game = data[0];
        String template = data[1];
        boolean flag = Boolean.parseBoolean(data[2]);

        AbstractGame gameObject = this.hub.getGameManager().getGameByIdentifier(game);

        if (gameObject == null)
            return;

        List<GameSign> sign = gameObject.getGameSignsByTemplate(template);

        if (sign == null)
            return;

        sign.forEach(gameSign -> gameSign.setSoon(flag));
    }
}
