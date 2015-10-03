package net.samagames.hub.common.hydroconnect;

import net.samagames.hub.Hub;
import net.samagames.hub.common.hydroconnect.connection.AbstractPacket;
import net.samagames.hub.common.hydroconnect.utils.PacketCallBack;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

/**
 * This file is a part of the SamaGames Project CodeBase
 * This code is absolutely confidential.
 * Created by Geekpower14 on 01/09/2015.
 * (C) Copyright Elydra Network 2014 & 2015
 * All rights reserved.
 */
public class PacketReceiver {

    public List<PacketCallBack> callbacks = new ArrayList<>();
    private Hub plugin;
    private HydroManager manager;

    public PacketReceiver(Hub plugin, HydroManager manager)
    {

        this.plugin = plugin;
        this.manager = manager;
    }

    public void registerCallBack(PacketCallBack callBack)
    {
        callbacks.add(callBack);
    }

    public void clearCallbacks()
    {
        callbacks.clear();
    }

    public void callPacket(AbstractPacket packet)
    {
        callbacks.stream().filter(callBack -> callBack.getPacketClass().getName().equals(packet.getClass().getName())).forEach(callBack -> Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> callBack.call(packet)));
    }
}
