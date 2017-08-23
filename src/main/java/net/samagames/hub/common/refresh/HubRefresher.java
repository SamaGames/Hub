package net.samagames.hub.common.refresh;

import com.google.gson.Gson;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.pubsub.IPacketsReceiver;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.main.GuiSwitchHub;
import redis.clients.jedis.Jedis;

/*
 * This file is part of Hub.
 *
 * Hub is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hub is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hub.  If not, see <http://www.gnu.org/licenses/>.
 */
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

        if (jedis == null)
            return;

        try
        {
            JsonHub thisHub = new JsonHub();
            thisHub.setHubNumber(Integer.parseInt(SamaGamesAPI.get().getServerName().split("_")[1]));
            thisHub.setConnectedPlayers(this.hub.getServer().getOnlinePlayers().size());
            this.hub.getServer().getOnlinePlayers().forEach(thisHub::addConnectedPlayer);

            jedis.publish("hub-status", new Gson().toJson(thisHub));

            this.hubs.update();
        }
        catch (Exception e)
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
        if (this.hubs.containsKey(id))
            return this.hubs.get(id);

        return null;
    }

    public CacheList getHubs()
    {
        return this.hubs;
    }
}
