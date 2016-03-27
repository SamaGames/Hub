package net.samagames.hub.common.hydroangeas.packets;

import net.samagames.hub.common.hydroangeas.connection.Packet;

public abstract class PacketCallBack<PACKET extends Packet>
{
    private Class<? extends Packet> packet;

    public PacketCallBack(Class<? extends Packet> packet)
    {
        this.packet = packet;
    }

    public abstract void call(PACKET packet);

    public Class getPacketClass()
    {
        return packet;
    }
}
