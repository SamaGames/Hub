package net.samagames.hub.interactions.graou;

import net.minecraft.server.v1_10_R1.PathEntity;
import net.samagames.hub.Hub;
import net.samagames.hub.utils.ProximityUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

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
        this.graou.toggleHolograms(false);

        this.player.setWalkSpeed(0.0F);
        this.player.setAllowFlight(false);
        this.player.setFlying(false);

        this.graou.getGraouEntity().setSitting(false);

        this.walk(this.treasureLocation);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Optional<Entity> entity = ProximityUtils.getNearbyEntities(OpeningAnimationRunnable.this.treasureLocation, 3.0D).stream()
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
        this.treasureLocation.getWorld().playSound(this.treasureLocation, Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);

        this.hub.getServer().getScheduler().runTaskLater(this.hub, () ->
        {
            this.treasureLocation.getWorld().playSound(this.treasureLocation, Sound.BLOCK_CHEST_CLOSE, 1.0F, 1.0F);
            this.walk(this.initialLocation);

            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    Optional<Entity> entity = ProximityUtils.getNearbyEntities(OpeningAnimationRunnable.this.initialLocation, 2.0D).stream()
                            .filter(e -> e.getUniqueId() == OpeningAnimationRunnable.this.graou.getGraouEntity().getUniqueID())
                            .findAny();

                    if (entity.isPresent())
                    {
                        OpeningAnimationRunnable.this.backToInitial();
                        this.cancel();
                    }
                }
            }.runTaskTimer(this.hub, 5L, 5L);
        }, 15L);
    }

    private void backToInitial()
    {
        this.graou.toggleHolograms(true);

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
