package net.samagames.hub.cosmetics.gadgets.displayers;

import net.samagames.hub.Hub;
import net.samagames.tools.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Random;

public class EnderSwapDisplayer extends AbstractDisplayer
{
    private final ArrayList<Location> teleportPositions;
    private BukkitTask teleportTask;

    public EnderSwapDisplayer(Player player)
    {
        super(player);

        this.teleportPositions = new ArrayList<>();

        Random random = new Random();
        Location last = this.baseLocation;

        for(int i = 0; i < 5; i++)
        {
            Location randomizedLocation = last.clone().add(random.nextInt(80) - 40, 0, random.nextInt(80) - 40);
            randomizedLocation.setY(256);

            while(randomizedLocation.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR)
            {
                randomizedLocation.subtract(0.0D, 1.0D, 0.0D);

                if(randomizedLocation.getBlockY() < 10)
                {
                    randomizedLocation.setY(256);
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
        this.teleportTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Hub.getInstance(), () ->
        {
            ParticleEffect.PORTAL.display(0.5F, 0, 0.5F, 0.3F, 5, this.player.getLocation().clone().add(0.0D, 1D, 0.0D));

            Bukkit.getScheduler().runTaskLater(Hub.getInstance(), () ->
            {
                this.player.getLocation().getWorld().playSound(this.player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                this.player.teleport(this.teleportPositions.get(0));

                this.teleportPositions.remove(0);

                if(this.teleportPositions.isEmpty())
                {
                    this.end();
                    this.callback();
                }
            }, 20L);

        }, 0L, 20L * 3);
    }

    @Override
    public void handleInteraction(Entity who, Entity with) {}

    @Override
    public boolean canUse()
    {
        return true;
    }

    private void callback()
    {
        this.teleportTask.cancel();
    }
}
