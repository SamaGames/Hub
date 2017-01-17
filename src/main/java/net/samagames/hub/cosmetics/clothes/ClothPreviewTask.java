package net.samagames.hub.cosmetics.clothes;

import net.minecraft.server.v1_10_R1.World;
import net.samagames.hub.Hub;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
    private static final double RADIUS = 4.5D;

    private final Location center;
    private final EntityClothCamera camera;
    private double i;

    public ClothPreviewTask(Hub hub, Location center)
    {
        this.center = center;

        World world = ((CraftWorld) hub.getWorld()).getHandle();
        this.camera = new EntityClothCamera(world);

        this.camera.setPosition(center.getX(), center.getY(), center.getZ());
        world.addEntity(this.camera, CreatureSpawnEvent.SpawnReason.CUSTOM);
        ((Guardian) this.camera.getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));

        this.runTaskTimer(hub, 2L, 2L);
    }

    @Override
    public void run()
    {
        Location location = new Location(this.center.getWorld(), this.center.getX() + Math.cos(this.i) * RADIUS, this.center.getY() + 0.15D, this.center.getZ() + Math.sin(this.i) * RADIUS);
        location.setDirection(location.subtract(this.center).toVector());

        this.camera.getBukkitEntity().teleport(location);

        this.i += 0.25D;

        if (this.i > Math.PI * 2.0D)
            this.i = 0.0D;
    }

    public void stop()
    {
        this.camera.die();
        this.cancel();
    }

    public EntityClothCamera getCamera()
    {
        return this.camera;
    }
}
