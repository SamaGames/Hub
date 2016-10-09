package net.samagames.hub.interactions.graou;

import net.minecraft.server.v1_10_R1.PathEntity;
import net.samagames.hub.Hub;
import org.bukkit.Location;
import org.bukkit.entity.Player;

class OpeningAnimationRunnable implements Runnable
{
    private final Hub hub;
    private final Graou graou;
    private final Player player;
    private final Location initialLocation;
    private final Location treasureLocation;

    OpeningAnimationRunnable(Hub hub, Graou graou, Player player, Location initialLocation, Location treasureLocation)
    {
        this.hub = hub;
        this.graou = graou;
        this.player = player;
        this.initialLocation = initialLocation;
        this.treasureLocation = treasureLocation;
    }

    @Override
    public void run()
    {
        PathEntity path = this.graou.getGraouEntity().getNavigation().a(this.treasureLocation.getX(), this.treasureLocation.getY(), this.treasureLocation.getZ());

        if (path == null)
        {
            this.graou.animationFinished(this.player);
            return;
        }

        this.graou.toggleHolograms(false);

        this.player.setWalkSpeed(0.0F);
        this.player.setAllowFlight(false);
        this.player.setFlying(false);

        this.graou.getGraouEntity().getGoalSit().setSitting(false);
        this.graou.getGraouEntity().getNavigation().a(path, 0.75D);


    }
}
