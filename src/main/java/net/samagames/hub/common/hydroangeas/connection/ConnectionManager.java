package net.samagames.hub.common.hydroangeas.connection;

import com.google.gson.Gson;
import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.common.hydroangeas.HydroangeasManager;
import net.samagames.hub.common.hydroangeas.packets.hubinfos.GameInfoToHubPacket;
import net.samagames.hub.common.hydroangeas.packets.queues.*;
import net.samagames.hub.common.managers.AbstractManager;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class ConnectionManager extends AbstractManager
{
    private final HydroangeasManager manager;
    private final Gson gson;
    private final Packet[] packets;

    public ConnectionManager(Hub hub, HydroangeasManager manager)
    {
        super(hub);

        this.manager = manager;
        this.gson = new Gson();

        this.packets = new Packet[256];

        // Queues Packets
        this.packets[100] = new QueueAddPlayerPacket();
        this.packets[101] = new QueueRemovePlayerPacket();
        this.packets[102] = new QueueAttachPlayerPacket();
        this.packets[103] = new QueueDetachPlayerPacket();
        this.packets[104] = new QueueInfosUpdatePacket();

        // HubInfos
        this.packets[110] = new GameInfoToHubPacket();
    }

    @Override
    public void onDisable() { /** Not needed **/ }

    @Override
    public void onLogin(Player player) { /** Not needed **/ }

    @Override
    public void onLogout(Player player) { /** Not needed **/ }

    public void getPacket(String packet)
    {
        String id;

        try
        {
            id = packet.split(":")[0];

            if (id == null || this.packets[Integer.parseInt(id)] == null)
            {
                this.log(Level.SEVERE, "Error bad packet ID in the channel");
                return;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.log(Level.SEVERE, "Error packet no ID in the channel");

            return;
        }

        packet = packet.substring(id.length()+1, packet.length());

        this.handler(Integer.valueOf(id), packet);
    }

    public int packetId(Packet p)
    {
        for (int i = 0; i < this.packets.length; i++)
        {
            if (this.packets[i] == null)
                continue;

            if (this.packets[i].getClass().equals(p.getClass()))
                return i;
        }

        return -1;
    }

    public void sendPacket(String channel, Packet data)
    {
        int id = this.packetId(data);

        if (id < 0)
        {
            this.log(Level.SEVERE, "Bad packet ID: " + id);
            return;
        }
        else if (channel == null)
        {
            this.log(Level.SEVERE, "Channel null !");
            return;
        }

        try
        {
            SamaGamesAPI.get().getPubSub().send(channel, id + ":" + this.gson.toJson(data));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void sendPacket(Packet packet)
    {
        this.sendPacket("global@hydroangeas-server", packet);
    }

    public void handler(int id, String data)
    {
        try
        {
            this.manager.getPacketReceiver().callPacket(this.gson.fromJson(data, this.packets[id].getClass()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
