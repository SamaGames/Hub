package net.samagames.hub.common.hydroangeas.packets.queues;

import net.samagames.hub.common.hydroangeas.QPlayer;

public class QueueRemovePlayerPacket extends QueuePacket
{
    private QPlayer player;

    public QueueRemovePlayerPacket() {}

    public QueueRemovePlayerPacket(QPlayer player)
    {
        this.player = player;
    }

    public QPlayer getPlayer()
    {
        return this.player;
    }
}
