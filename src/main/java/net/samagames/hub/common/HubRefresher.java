package net.samagames.hub.common;

import com.google.gson.Gson;
import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HubRefresher implements Runnable
{
    private static final String REDIS_KEY = "hubs_connected";

    private final Hub hub;
    private final List<JsonHub> hubs;

    public HubRefresher(Hub hub)
    {
        this.hub = hub;
        this.hubs = new ArrayList<>();
    }

    @Override
    public void run()
    {
        Jedis jedis = SamaGamesAPI.get().getBungeeResource();

        try
        {
            JsonHub thisHub = new JsonHub();
            thisHub.setHubNumber(Integer.parseInt(SamaGamesAPI.get().getServerName().split("_")[1]));
            thisHub.setConnectedPlayers(this.hub.getServer().getOnlinePlayers().size());
            this.hub.getServer().getOnlinePlayers().forEach(thisHub::addConnectedPlayer);

            String thisHubJson = new Gson().toJson(thisHub);

            jedis.hset(REDIS_KEY, SamaGamesAPI.get().getServerName(), thisHubJson);

            if(jedis.exists(REDIS_KEY))
            {
                Map<String, String> redisHubs = jedis.hgetAll(REDIS_KEY);
                HashMap<Integer, String> hubsList = new HashMap<>();

                for (String hubServerName : redisHubs.keySet())
                    hubsList.put(Integer.parseInt(hubServerName.split("_")[1]), redisHubs.get(hubServerName));

                this.hubs.clear();

                for (int hubNumber : hubsList.keySet())
                {
                    String jsonHubString = hubsList.get(hubNumber);
                    JsonHub jsonHub = new Gson().fromJson(jsonHubString, JsonHub.class);
                    this.hubs.add(jsonHub);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        jedis.close();

        //TODO this.hub.getGuiManager().getPlayersGui().keySet().stream().filter(uuid -> this.hub.getGuiManager().getPlayersGui().get(uuid) instanceof GuiSwitchHub).forEach(uuid -> this.hub.getGuiManager().getPlayersGui().get(uuid).update(this.hub.getServer().getPlayer(uuid)));
    }

    public void removeFromList()
    {
        Jedis jedis = SamaGamesAPI.get().getBungeeResource();
        jedis.hdel(REDIS_KEY, SamaGamesAPI.get().getServerName());
        jedis.close();
    }

    public JsonHub getHubByID(int id)
    {
        for(JsonHub jsonHub : this.hubs)
            if(jsonHub.getHubNumber() == id)
                return jsonHub;

        return null;
    }

    public List<JsonHub> getHubs()
    {
        return this.hubs;
    }
}
