package net.samagames.hub.hostgame;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.samagames.hub.Hub;
import net.samagames.hub.common.hydroangeas.packets.PacketCallBack;
import net.samagames.hub.common.hydroangeas.packets.hubinfos.HostGameInfoToHubPacket;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.tools.JsonConfiguration;
import net.samagames.tools.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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

    private final Map<UUID, NPCHostGame> hosts;
    private final List<Location> positions;
    private boolean[] available;

    public HostGameManager(Hub hub)
    {
        super(hub);

        this.hosts = new HashMap<>();
        this.positions = new ArrayList<>();

        JsonObject config = this.reloadConfiguration();

        if(config == null)
            return;

        JsonArray locations = config.get("locations").getAsJsonArray();

        for (JsonElement location : locations)
            this.positions.add(LocationUtils.str2loc(location.getAsString()));

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

        while (!this.available[i] && i < this.available.length-1) // If no available we use the last position in the array
        {
            i++;
        }

        this.available[i] = false;
        this.hosts.put(packet.getEvent(), new NPCHostGame(this.positions.get(i), packet));
    }

    private void updateHost(HostGameInfoToHubPacket packet)
    {
        NPCHostGame npcHostGame = this.hosts.get(packet.getEvent());

        if (npcHostGame != null)
            npcHostGame.update(packet);
    }

    private void removeHost(UUID event)
    {
        NPCHostGame npcHostGame = this.hosts.get(event);

        if (npcHostGame != null)
            npcHostGame.remove();
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

    private JsonObject reloadConfiguration()
    {
        File configuration = new File(this.hub.getDataFolder(), "hostgame.json");

        if (!configuration.exists())
        {
            try(PrintWriter writer = new PrintWriter(configuration))
            {
                configuration.createNewFile();
                writer.println("{}");
                writer.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        JsonConfiguration npcsConfig = new JsonConfiguration(configuration);
        return npcsConfig.load();
    }
}
