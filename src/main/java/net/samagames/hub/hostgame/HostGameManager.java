package net.samagames.hub.hostgame;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.samagames.hub.Hub;
import net.samagames.hub.common.hydroangeas.packets.PacketCallBack;
import net.samagames.hub.common.hydroangeas.packets.hubinfos.HostGameInfoToHubPacket;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.tools.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
public class HostGameManager extends AbstractManager {
    private static final UUID AURELIEN_SAMA_UUID = UUID.fromString("c59220b1-662f-4aa8-b9d9-72660eb97c10");

    private HashMap<UUID, NPCHostGame> hosts = new HashMap<>();

    private List<Location> positions = new ArrayList<>();

    private boolean[] available;

    public HostGameManager(Hub hub) {
        super(hub, "hostgame.json");

        JsonObject config = this.reloadConfiguration();
        if(config == null)
            return;

        JsonArray locations = config.get("locations").getAsJsonArray();

        for (JsonElement loc : locations)
        {
            positions.add(LocationUtils.str2loc(loc.getAsString()));
        }

        available = new boolean[positions.size()];
        for ( int i = 0; i < positions.size(); i++)
        {
            available[i] = true;
        }

        hub.getHydroangeasManager().getPacketReceiver().registerCallBack(new PacketCallBack<HostGameInfoToHubPacket>(HostGameInfoToHubPacket.class) {
            @Override
            public void call(HostGameInfoToHubPacket packet) {
                switch (packet.getState())
                {
                    case 0:
                        addHost(packet);
                        break;
                    case 1:
                        updateHost(packet);
                        break;
                    default:
                        removeHost(packet.getEvent());
                        break;
                }
            }
        });
    }

    private void addHost(HostGameInfoToHubPacket packet)
    {
        int i = 0;
        while (!available[i] && i < available.length-1) // If no available we use the last position in the array
        {
            i++;
        }
        available[i] = false;

        hosts.put(packet.getEvent(), new NPCHostGame(positions.get(i), packet));
    }

    private void updateHost(HostGameInfoToHubPacket packet)
    {
        NPCHostGame npcHostGame = hosts.get(packet.getEvent());

        if (npcHostGame != null)
        {
            npcHostGame.update(packet);
        }
    }

    private void removeHost(UUID event)
    {
        NPCHostGame npcHostGame = hosts.get(event);

        if (npcHostGame != null)
        {
            npcHostGame.remove();
        }
    }

    @Override
    public void onDisable() {
        hosts.values().forEach(NPCHostGame::remove);
    }

    @Override
    public void onLogin(Player player) {

    }

    @Override
    public void onLogout(Player player) {

    }


}
