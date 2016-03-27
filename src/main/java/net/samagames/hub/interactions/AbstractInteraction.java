package net.samagames.hub.interactions;

import net.samagames.hub.Hub;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class AbstractInteraction implements Listener
{
    protected final Hub hub;

    public AbstractInteraction(Hub hub)
    {
        this.hub = hub;
    }

    public abstract void onDisable();

    public abstract void play(Player player);
    public abstract void stop(Player player);

    public abstract boolean hasPlayer(Player player);
}
