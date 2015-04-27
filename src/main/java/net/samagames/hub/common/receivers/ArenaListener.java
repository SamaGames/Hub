package net.samagames.hub.common.receivers;

import com.google.gson.Gson;
import net.samagames.api.channels.PacketsReceiver;
import net.samagames.api.games.ServerStatus;
import net.samagames.hub.Hub;

public class ArenaListener implements PacketsReceiver
{
    @Override
    public void receive(String channel, String packet)
    {
        ServerStatus data = new Gson().fromJson(packet, ServerStatus.class);

        String game = data.getGame();
        String map = data.getMap();

        Hub.getInstance().getGameManager().getGameByIdentifier(game).getGameSignByMap(map).update(data);
    }
}
