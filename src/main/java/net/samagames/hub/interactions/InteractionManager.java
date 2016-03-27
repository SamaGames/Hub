package net.samagames.hub.interactions;

import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.interactions.yodels.YodelManager;
import org.bukkit.entity.Player;

public class InteractionManager
{
    private final YodelManager yodelManager;

    public InteractionManager(Hub hub)
    {
        this.yodelManager = new YodelManager(hub);
    }

    public YodelManager getYodelManager()
    {
        return this.yodelManager;
    }
}
