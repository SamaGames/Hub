package net.samagames.hub.hostgame;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.samagames.hub.Hub;
import net.samagames.hub.common.hydroangeas.packets.PacketCallBack;
import net.samagames.hub.common.hydroangeas.packets.hubinfos.HostGameInfoToHubPacket;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.tools.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * ╱╲＿＿＿＿＿＿╱╲
 * ▏╭━━╮╭━━╮▕
 * ▏┃＿＿┃┃＿＿┃▕
 * ▏┃＿▉┃┃▉＿┃▕
 * ▏╰━━╯╰━━╯▕
 * ╲╰╰╯╲╱╰╯╯╱  Created by Silvanosky on 27/10/2016
 * ╱╰╯╰╯╰╯╰╯╲
 * ▏▕╰╯╰╯╰╯▏▕
 * ▏▕╯╰╯╰╯╰▏▕
 * ╲╱╲╯╰╯╰╱╲╱
 * ＿＿╱▕▔▔▏╲＿＿
 * ＿＿▔▔＿＿▔▔＿＿
 */
public class HostGameManager extends AbstractManager
{
    private static final UUID AURELIEN_SAMA_UUID = UUID.fromString("c59220b1-662f-4aa8-b9d9-72660eb97c10");

    private final Map<UUID, NPCHostGame> hosts = new HashMap<>();
    private final List<Location> positions = new ArrayList<>();
    private boolean[] available;

    public HostGameManager(Hub hub)
    {
        super(hub, "hostgame.json");

        JsonObject config = this.reloadConfiguration();

        if(config == null)
            return;

        JsonArray locations = config.get("locations").getAsJsonArray();

        for (JsonElement location : locations)
        {
            Location loc = LocationUtils.str2loc(location.getAsString());
            hub.getLogger().info("Add new location for host game: " + loc);
            this.positions.add(loc);
        }

        this.available = new boolean[this.positions.size()];

        for ( int i = 0; i < this.positions.size(); i++)
            this.available[i] = true;

        hub.getHydroangeasManager().getPacketReceiver().registerCallBack(new PacketCallBack<HostGameInfoToHubPacket>(HostGameInfoToHubPacket.class)
        {
            @Override
            public void call(HostGameInfoToHubPacket packet)
            {
                switch (packet.getState())
                {
                    case 0:
                    case 1:
                        Bukkit.getScheduler().runTask(hub, () -> updateHost(packet));
                        break;
                    default:
                        Bukkit.getScheduler().runTask(hub, () -> removeHost(packet.getEvent()));
                        break;
                }
            }
        });
    }

    private NPCHostGame addHost(HostGameInfoToHubPacket packet)
    {
        this.hub.getLogger().info("Adding new host game !");

        int i = 0;

        while (!this.available[i] && i < this.available.length-1) // If no available we use the last position in the array
        {
            i++;
        }

        this.available[i] = false;
        return this.hosts.put(packet.getEvent(), new NPCHostGame(this.hub, i, this.positions.get(i), packet));
    }

    private void updateHost(HostGameInfoToHubPacket packet)
    {
        NPCHostGame npcHostGame = this.hosts.get(packet.getEvent());

        if (npcHostGame == null)
            npcHostGame = addHost(packet);

        npcHostGame.update(packet);
    }

    private void removeHost(UUID event)
    {
        NPCHostGame npcHostGame = this.hosts.get(event);

        if (npcHostGame != null)
        {
            npcHostGame.remove();
            this.available[npcHostGame.getIndex()] = true;
        }
    }

    @Override
    public void onDisable()
    {
        hosts.values().forEach(NPCHostGame::remove);
    }

    @Override
    public void onLogin(Player player) { /** Not needed **/ }

    @Override
    public void onLogout(Player player) { /** Not needed **/ }
}