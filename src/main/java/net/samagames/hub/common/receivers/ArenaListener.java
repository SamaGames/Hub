package net.samagames.hub.common.receivers;

import com.google.gson.Gson;
import net.samagames.api.pubsub.IPacketsReceiver;
import net.samagames.core.api.games.ServerStatus;
import net.samagames.hub.Hub;

import java.util.logging.Level;

public class ArenaListener implements IPacketsReceiver
{
    @Override
    public void receive(String channel, String packet)
    {
        ServerStatus data = new Gson().fromJson(packet, ServerStatus.class);

        String game = data.getGame();
        String map = data.getMap();

        if(Hub.getInstance().getGameManager().getGameByIdentifier(game) == null)
        {
            Hub.getInstance().log(Level.SEVERE, "Received a server data with an unknown game! (" + game + ")");
            return;
        }
        else if(Hub.getInstance().getGameManager().getGameByIdentifier(game).getGameSignByMap(map) == null)
        {
            Hub.getInstance().log(Level.SEVERE, "Received a server data for the game '" + game + "' with an unknown map! (" + map + ")");
            return;
        }

        Hub.getInstance().getGameManager().getGameByIdentifier(game).getGameSignByMap(map).update(data);
    }
}
