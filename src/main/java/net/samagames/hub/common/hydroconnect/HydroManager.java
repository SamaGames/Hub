package net.samagames.hub.common.hydroconnect;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.common.hydroconnect.connection.ConnectionManager;
import net.samagames.hub.common.hydroconnect.packets.queues.QueueAddPlayerPacket;
import net.samagames.hub.common.hydroconnect.packets.queues.QueueAttachPlayerPacket;
import net.samagames.hub.common.hydroconnect.packets.queues.QueuePacket;
import net.samagames.hub.common.hydroconnect.packets.queues.QueueRemovePlayerPacket;
import net.samagames.hub.common.hydroconnect.queue.QPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This file is a part of the SamaGames Project CodeBase
 * This code is absolutely confidential.
 * Created by Geekpower14 on 17/07/2015.
 * (C) Copyright Elydra Network 2014 & 2015
 * All rights reserved.
 */
public class HydroManager {

    private Hub plugin;

    private ConnectionManager connectionManager;

    public HydroManager(Hub plugin)
    {

        this.plugin = plugin;

        connectionManager = new ConnectionManager(plugin);
    }

    public void rejoinQueueToLeader(UUID leader, UUID player)
    {
        List<QPlayer> list = new ArrayList<>();
        list.add(new QPlayer(player, getPriority(player)));
        connectionManager.sendPacket(new QueueAttachPlayerPacket(new QPlayer(leader, getPriority(leader)), list));
    }

    public void removePlayerFromQueues(UUID uuid)
    {
        connectionManager.sendPacket(new QueueRemovePlayerPacket(new QPlayer(uuid, getPriority(uuid))));
    }

    public void addPlayerToQueue(UUID player, String game, String map)
    {
        QPlayer qPlayer = new QPlayer(player, getPriority(player));
        connectionManager.sendPacket(new QueueAddPlayerPacket(QueuePacket.TypeQueue.NAMED,
                game,
                map,
                qPlayer));
    }

    public void addPlayerToQueue(UUID player, String templateID)
    {
        QPlayer qPlayer = new QPlayer(player, getPriority(player));
        connectionManager.sendPacket(new QueueAddPlayerPacket(QueuePacket.TypeQueue.NAMEDID,
                templateID,
                qPlayer));
    }

    public void addPartyToQueue(UUID leader, UUID party, String game, String map)
    {
        HashMap<UUID, String> playersInParty = SamaGamesAPI.get().getPartiesManager().getPlayersInParty(party);

        List<QPlayer> players = playersInParty.keySet().stream().map(player -> new QPlayer(player, getPriority(player))).collect(Collectors.toList());
        QPlayer qPlayer = new QPlayer(leader, getPriority(leader));

        addPlayerToQueue(leader, game, map);

        connectionManager.sendPacket(new QueueAttachPlayerPacket(qPlayer, players));
    }

    public void addPartyToQueue(UUID leader, UUID party, String templateID)
    {
        HashMap<UUID, String> playersInParty = SamaGamesAPI.get().getPartiesManager().getPlayersInParty(party);

        List<QPlayer> players = playersInParty.keySet().stream().map(player -> new QPlayer(player, getPriority(player))).collect(Collectors.toList());
        QPlayer qPlayer = new QPlayer(leader, getPriority(leader));

        addPlayerToQueue(leader, templateID);

        connectionManager.sendPacket(new QueueAttachPlayerPacket(qPlayer, players));
    }

    public int getPriority(UUID uuid)
    {
        return SamaGamesAPI.get().getPermissionsManager().getApi().getUser(uuid).getParents().first().getLadder();
    }
}
