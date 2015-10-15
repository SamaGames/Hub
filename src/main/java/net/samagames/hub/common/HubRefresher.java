package net.samagames.hub.common;

import com.google.gson.Gson;
import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.main.GuiSwitchHub;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HubRefresher implements Runnable
{
    private final Hub hub;
    private final ArrayList<JsonHub> hubs;

    public HubRefresher(Hub hub)
    {
        this.hub = hub;
        this.hubs = new ArrayList<>();
    }

    @Override
    public void run()
    {
        Jedis jedis = SamaGamesAPI.get().getBungeeResource();

        try{
            JsonHub thisHub = new JsonHub();
            thisHub.setHubNumber(Integer.parseInt(SamaGamesAPI.get().getServerName().split("_")[1]));
            thisHub.setConnectedPlayers(Bukkit.getOnlinePlayers().size());
            Bukkit.getOnlinePlayers().forEach(thisHub::addConnectedPlayer);

            String thisHubJson = new Gson().toJson(thisHub);
            jedis.hdel("hubs_connected", SamaGamesAPI.get().getServerName());
            if(jedis.hexists("hubs_connected", SamaGamesAPI.get().getServerName()))
            {
                jedis.hset("hubs_connected", SamaGamesAPI.get().getServerName(), thisHubJson);
            }

            Map<String, String> redisHubs = jedis.hgetAll("hubs_connected");
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
        }catch(Exception e)
        {
            e.printStackTrace();
        }finally {
            jedis.close();
        }

        this.hub.getGuiManager().getPlayersGui().keySet().stream().filter(uuid -> this.hub.getGuiManager().getPlayersGui().get(uuid) instanceof GuiSwitchHub).forEach(uuid -> this.hub.getGuiManager().getPlayersGui().get(uuid).update(Bukkit.getPlayer(uuid)));
    }

    public void removeFromList()
    {
        Jedis jedis = SamaGamesAPI.get().getBungeeResource();
        jedis.hdel("hubs_connected", SamaGamesAPI.get().getServerName());
        jedis.close();
    }

    public JsonHub getHubByID(int id)
    {
        for(JsonHub hub : this.hubs)
            if(hub.getHubNumber() == id)
                return hub;

        return null;
    }

    public ArrayList<JsonHub> getHubs()
    {
        return this.hubs;
    }
}
