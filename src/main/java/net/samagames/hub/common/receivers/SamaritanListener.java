package net.samagames.hub.common.receivers;

import net.samagames.api.pubsub.IPacketsReceiver;
import net.samagames.hub.Hub;
import org.bukkit.World;

public class SamaritanListener implements IPacketsReceiver
{
    private static final long DAY = 6000L;
    private static final long NIGHT = 18000L;

    private final Hub hub;

    public SamaritanListener(Hub hub)
    {
        this.hub = hub;
    }

    @Override
    public void receive(String channel, String packet)
    {
        String[] splited = packet.split("#####");
        String player = splited[0];
        String reason = splited[1];

        World world = this.hub.getWorld();
        world.setTime(world.getTime() == DAY ? NIGHT : DAY);

        // TODO
    }
}
