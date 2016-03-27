package net.samagames.hub.common.hydroangeas;

import net.samagames.hub.Hub;
import net.samagames.hub.common.hydroangeas.connection.Packet;
import net.samagames.hub.common.hydroangeas.packets.PacketCallBack;

import java.util.ArrayList;
import java.util.List;

public class PacketReceiver
{
    private final Hub plugin;
    private final List<PacketCallBack> callbacks;

    public PacketReceiver(Hub plugin)
    {
        this.plugin = plugin;
        this.callbacks = new ArrayList<>();
    }

    public void registerCallBack(PacketCallBack callBack)
    {
        this.callbacks.add(callBack);
    }

    public void clearCallbacks()
    {
        this.callbacks.clear();
    }

    public void callPacket(Packet packet)
    {
        this.callbacks.stream().filter(callBack -> callBack.getPacketClass().getName().equals(packet.getClass().getName())).forEach(callBack -> this.plugin.getScheduledExecutorService().execute(() -> callBack.call(packet)));
    }
}
