package net.samagames.hub.cosmetics.gadgets.displayers;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.gadgets.GadgetManager;
import net.samagames.tools.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class EnderSwapDisplayer extends AbstractDisplayer
{
    private final List<Location> teleportPositions;
    private BukkitTask teleportTask;

    public EnderSwapDisplayer(Hub hub, Player player)
    {
        super(hub, player);

        this.teleportPositions = new ArrayList<>();

        Location last = this.baseLocation;

        for(int i = 0; i < 10; i++)
        {
            Location randomizedLocation = last.clone().add(GadgetManager.RANDOM.nextInt(80) - 40, 0, GadgetManager.RANDOM.nextInt(80) - 40);
            randomizedLocation.setY(250);

            while(randomizedLocation.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR)
            {
                randomizedLocation.subtract(0.0D, 1.0D, 0.0D);

                if(randomizedLocation.getBlockY() < 10)
                {
                    randomizedLocation.setY(250);
                    break;
                }
            }

            this.teleportPositions.add(randomizedLocation);
            last = randomizedLocation;
        }
    }

    @Override
    public void display()
    {
        this.player.playSound(this.player.getLocation(), Sound.ENTITY_ENDERMEN_AMBIENT, 1.0F, 1.0F);

        this.teleportTask = this.hub.getServer().getScheduler().runTaskTimerAsynchronously(this.hub, () ->
        {
            ParticleEffect.PORTAL.display(0.75F, 0, 0.75F, 1.5F, 20, this.player.getLocation().clone().add(0.0D, 1D, 0.0D), 100.0D);

            this.hub.getServer().getScheduler().runTaskLater(this.hub, () ->
            {
                this.player.getLocation().getWorld().playSound(this.player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);

                this.hub.getServer().getScheduler().runTaskLater(this.hub, () ->
                {
                    this.player.teleport(this.teleportPositions.get(0));

                    this.teleportPositions.remove(0);

                    if(this.teleportPositions.isEmpty())
                    {
                        this.end();
                        this.teleportTask.cancel();
                    }
                }, 5L);
            }, 10L);

        }, 0L, 20L * 2);
    }

    @Override
    public void handleInteraction(Entity who, Entity with) {}

    @Override
    public boolean isInteractionsEnabled()
    {
        return false;
    }

    @Override
    public boolean canUse()
    {
        return true;
    }
}
