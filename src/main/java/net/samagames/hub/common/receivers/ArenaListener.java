package net.samagames.hub.common.receivers;

import com.google.gson.Gson;
import net.samagames.api.channels.PacketsReceiver;
import net.samagames.api.signs.SignData;
import net.samagames.hub.Hub;

public class ArenaListener implements PacketsReceiver
{
    @Override
    public void receive(String channel, String packet)
    {
        SignData data = new Gson().fromJson(packet, SignData.class);

        String game = data.getGameType();
        String map = data.getMap();

        Hub.getInstance().getGameManager().getGameByIdentifier(game).getGameSignZoneByMap(map).updateSign(data);
    }
}
