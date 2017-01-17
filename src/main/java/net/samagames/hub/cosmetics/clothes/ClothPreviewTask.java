package net.samagames.hub.cosmetics.clothes;

import net.samagames.hub.Hub;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 17/01/2017
 */
class ClothPreviewTask extends BukkitRunnable
{
    private static final double RADIUS = 3.0D;

    private final Location center;
    private final ArmorStand armorStand;
    private double i;

    public ClothPreviewTask(Hub hub, Location center)
    {
        this.center = center;

        this.armorStand = hub.getWorld().spawn(this.center, ArmorStand.class);
        this.armorStand.setVisible(false);

        this.runTaskTimer(hub, 1L, 1L);
    }

    @Override
    public void run()
    {
        Location location = new Location(this.center.getWorld(), this.center.getX() + Math.cos(this.i) * RADIUS, this.center.getY() + 0.15D, this.center.getZ() + Math.sin(this.i) * RADIUS);
        location.setDirection(location.subtract(this.center).toVector());

        this.armorStand.setVelocity(location.toVector());

        this.i += 0.25D;

        if (this.i > Math.PI * 2.0D)
            this.i = 0.0D;
    }

    public void stop()
    {
        this.armorStand.remove();
        this.cancel();
    }

    public ArmorStand getArmorStand()
    {
        return this.armorStand;
    }
}
