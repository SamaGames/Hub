package net.samagames.hub.common.refresh;

import com.google.gson.Gson;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.pubsub.IPacketsReceiver;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.main.GuiSwitchHub;
import redis.clients.jedis.Jedis;

public class HubRefresher implements Runnable, IPacketsReceiver
{
    private final Hub hub;
    private final CacheList hubs;

    public HubRefresher(Hub hub)
    {
        this.hub = hub;
        this.hubs = new CacheList();
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

            jedis.publish("hub-status", new Gson().toJson(thisHub));

            this.hubs.update();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        jedis.close();

        this.hub.getGuiManager().getPlayersGui().keySet().stream().filter(uuid -> this.hub.getGuiManager().getPlayersGui().get(uuid) instanceof GuiSwitchHub).forEach(uuid -> this.hub.getGuiManager().getPlayersGui().get(uuid).update(this.hub.getServer().getPlayer(uuid)));
    }

    @Override
    public void receive(String channel, String data)
    {
        JsonHub jsonHub = new Gson().fromJson(data, JsonHub.class);
        this.hubs.put(jsonHub.getHubNumber(), jsonHub);
    }

    public JsonHub getHubByID(int id)
    {
        if(this.hubs.containsKey(id))
            return this.hubs.get(id);

        return null;
    }

    public CacheList getHubs()
    {
        return this.hubs;
    }
}
