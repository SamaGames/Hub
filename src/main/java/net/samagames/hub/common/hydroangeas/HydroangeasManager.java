package net.samagames.hub.common.hydroangeas;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.common.hydroangeas.connection.ConnectionManager;
import net.samagames.hub.common.hydroangeas.packets.other.CommandPacket;
import net.samagames.hub.common.hydroangeas.packets.queues.QueueAddPlayerPacket;
import net.samagames.hub.common.hydroangeas.packets.queues.QueueAttachPlayerPacket;
import net.samagames.hub.common.hydroangeas.packets.queues.QueuePacket;
import net.samagames.hub.common.hydroangeas.packets.queues.QueueRemovePlayerPacket;
import net.samagames.hub.common.managers.AbstractManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class HydroangeasManager extends AbstractManager
{
    private final ConnectionManager connectionManager;
    private final PacketReceiver packetReceiver;

    public HydroangeasManager(Hub hub)
    {
        super(hub);

        this.connectionManager = new ConnectionManager(hub, this);
        this.packetReceiver = new PacketReceiver(hub);

        SamaGamesAPI.get().getPubSub().subscribe("hydroHubReceiver", (channel, packet) ->
        {
            try
            {
                this.connectionManager.getPacket(packet);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onDisable() { /** Not needed **/ }

    @Override
    public void onLogin(Player player) { /** Not needed **/ }

    @Override
    public void onLogout(Player player) { /** Not needed **/ }

    public void rejoinQueueToLeader(UUID leader, UUID player)
    {
        List<QPlayer> list = new ArrayList<>();
        list.add(new QPlayer(player, getPriority(player)));

        this.connectionManager.sendPacket(new QueueAttachPlayerPacket(new QPlayer(leader, getPriority(leader)), list));
    }

    public void removePlayerFromQueues(UUID uuid)
    {
        this.connectionManager.sendPacket(new QueueRemovePlayerPacket(new QPlayer(uuid, getPriority(uuid))));
    }

    public void addPlayerToQueue(UUID player, String game, String map)
    {
        QPlayer qPlayer = new QPlayer(player, getPriority(player));
        this.connectionManager.sendPacket(new QueueAddPlayerPacket(QueuePacket.TypeQueue.NAMED, game, map, qPlayer));
    }

    public void addPlayerToQueue(UUID player, String templateID)
    {
        QPlayer qPlayer = new QPlayer(player, getPriority(player));
        this.connectionManager.sendPacket(new QueueAddPlayerPacket(QueuePacket.TypeQueue.NAMEDID, templateID, qPlayer));
    }

    public void addPartyToQueue(UUID leader, UUID party, String game, String map)
    {
        List<UUID> playersInParty = SamaGamesAPI.get().getPartiesManager().getPlayersInParty(party);

        List<QPlayer> players = playersInParty.stream().map(player -> new QPlayer(player, getPriority(player))).collect(Collectors.toList());
        QPlayer qPlayer = new QPlayer(leader, getPriority(leader));

        addPlayerToQueue(leader, game, map);

        this.connectionManager.sendPacket(new QueueAttachPlayerPacket(qPlayer, players));
    }

    public void addPartyToQueue(UUID leader, UUID party, String templateID)
    {
        List<UUID> playersInParty = SamaGamesAPI.get().getPartiesManager().getPlayersInParty(party);

        List<QPlayer> players = playersInParty.stream().map(player -> new QPlayer(player, getPriority(player))).collect(Collectors.toList());
        QPlayer qPlayer = new QPlayer(leader, getPriority(leader));

        addPlayerToQueue(leader, templateID);

        this.connectionManager.sendPacket(new QueueAttachPlayerPacket(qPlayer, players));
    }

    public void orderServer(String player, String template)
    {
        this.connectionManager.sendPacket(new CommandPacket(player, "order " + template));
    }

    public int getPriority(UUID uuid)
    {
        return SamaGamesAPI.get().getPermissionsManager().getPlayer(uuid).getRank();
    }

    public PacketReceiver getPacketReceiver()
    {
        return this.packetReceiver;
    }
}
