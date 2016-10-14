package net.samagames.hub.interactions.graou;

import net.minecraft.server.v1_10_R1.PathEntity;
import net.samagames.hub.Hub;
import net.samagames.hub.utils.ProximityUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

class OpeningAnimationRunnable implements Runnable
{
    private final Hub hub;
    private final Graou graou;
    private final Player player;
    private final Location[] treasureLocations;
    private final Location[] openingLocations;

    OpeningAnimationRunnable(Hub hub, Graou graou, Player player, Location[] treasureLocations, Location[] openingLocations)
    {
        this.hub = hub;
        this.graou = graou;
        this.player = player;
        this.treasureLocations = treasureLocations;
        this.openingLocations = openingLocations;
    }

    @Override
    public void run()
    {
        this.graou.toggleHolograms(false);

        this.player.setWalkSpeed(0.0F);
        this.player.setAllowFlight(false);
        this.player.setFlying(false);

        this.graou.getGraouEntity().setSitting(false);

        this.walk(this.treasureLocations[1]);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Optional<Entity> entity = ProximityUtils.getNearbyEntities(OpeningAnimationRunnable.this.treasureLocations[0], 3.0D).stream()
                        .filter(e -> e.getUniqueId() == OpeningAnimationRunnable.this.graou.getGraouEntity().getUniqueID())
                        .findAny();

                if (entity.isPresent())
                {
                    OpeningAnimationRunnable.this.arrivedAtTreasure();
                    this.cancel();
                }
            }
        }.runTaskTimer(this.hub, 5L, 5L);
    }

    private void arrivedAtTreasure()
    {
        this.treasureLocations[0].getWorld().playSound(this.treasureLocations[0], Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);

        this.hub.getServer().getScheduler().runTaskLater(this.hub, () ->
        {
            this.treasureLocations[0].getWorld().playSound(this.treasureLocations[0], Sound.BLOCK_CHEST_CLOSE, 1.0F, 1.0F);
            this.walk(this.openingLocations[1]);

            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    Optional<Entity> entity = ProximityUtils.getNearbyEntities(OpeningAnimationRunnable.this.openingLocations[0], 2.0D).stream()
                            .filter(e -> e.getUniqueId() == OpeningAnimationRunnable.this.graou.getGraouEntity().getUniqueID())
                            .findAny();

                    if (entity.isPresent())
                    {
                        OpeningAnimationRunnable.this.finish();
                        this.cancel();
                    }
                }
            }.runTaskTimer(this.hub, 5L, 5L);
        }, 15L);
    }

    private void finish()
    {
        this.graou.toggleHolograms(true);

        this.hub.getServer().broadcastMessage("Finished animation.");

        this.graou.respawn();
        this.graou.animationFinished(this.player);
    }

    private void walk(Location location)
    {
        PathEntity path = this.graou.getGraouEntity().getNavigation().a(location.getX(), location.getY(), location.getZ());

        if (path == null)
        {
            this.graou.respawn();
            this.graou.animationFinished(this.player);

            return;
        }

        this.graou.getGraouEntity().getNavigation().a(path, 1.0D);

    }
}
