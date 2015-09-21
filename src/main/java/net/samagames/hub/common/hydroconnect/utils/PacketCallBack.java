package net.samagames.hub.common.hydroconnect.utils;

import net.samagames.hub.common.hydroconnect.connection.AbstractPacket;

/**
 * This file is a part of the SamaGames Project CodeBase
 * This code is absolutely confidential.
 * Created by Geekpower14 on 01/09/2015.
 * (C) Copyright Elydra Network 2014 & 2015
 * All rights reserved.
 */
public abstract class PacketCallBack<PACKET extends AbstractPacket> {

    private Class<? extends AbstractPacket> packet;

    public PacketCallBack(Class<? extends AbstractPacket> packet)
    {
        this.packet = packet;
    }

    public abstract void call(PACKET packet);

    public Class getPacketClass()
    {
        return packet.getClass();
    }

}
