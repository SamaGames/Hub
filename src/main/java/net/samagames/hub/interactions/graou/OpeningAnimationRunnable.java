package net.samagames.hub.interactions.graou;

import net.samagames.hub.Hub;
import org.bukkit.entity.Player;

class OpeningAnimationRunnable implements Runnable
{
    private final Hub hub;
    private final Graou graou;
    private final Player player;

    OpeningAnimationRunnable(Hub hub, Graou graou, Player player)
    {
        this.hub = hub;
        this.graou = graou;
        this.player = player;
    }

    @Override
    public void run()
    {
        this.graou.animationFinished();
    }
}
