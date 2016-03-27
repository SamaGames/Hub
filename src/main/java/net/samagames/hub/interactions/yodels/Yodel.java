package net.samagames.hub.interactions.yodels;

import net.minecraft.server.v1_9_R1.WorldServer;
import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteraction;
import net.samagames.hub.utils.ProximityUtils;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class Yodel extends AbstractInteraction
{
    private final Map<UUID, BukkitTask> travellingTasks;
    private final Map<UUID, EntityYodel> vehicles;

    private final Location startRails;
    private final Location endRails;
    private final Location endPlatform;
    private final Vector direction;
    private final ArmorStand startBeacon;
    private final ArmorStand endBeacon;
    private final BukkitTask startTask;

    Yodel(Hub hub, String startRails, String endRails, Location startPlatform, Location endPlatform)
    {
        super(hub);

        this.travellingTasks = new HashMap<>();
        this.vehicles = new HashMap<>();
        this.endPlatform = endPlatform;

        ArmorStand startArmorStand = (ArmorStand) startPlatform.getWorld().getNearbyEntities(startPlatform, 8.0D, 8.0D, 8.0D).stream().filter(entity -> entity instanceof ArmorStand).filter(entity -> entity.getCustomName().equals(startRails)).findFirst().orElse(null);
        ArmorStand endArmorStand = (ArmorStand) endPlatform.getWorld().getNearbyEntities(endPlatform, 8.0D, 8.0D, 8.0D).stream().filter(entity -> entity instanceof ArmorStand).filter(entity -> entity.getCustomName().equals(endRails)).findFirst().orElse(null);

        if (startArmorStand == null || endArmorStand == null)
            throw new NullPointerException("One side of the yodel cannot be found!");

        this.startRails = startArmorStand.getLocation().clone().subtract(0.0D, 1.8D, 0.0D);
        this.endRails = endArmorStand.getLocation().clone().subtract(0.0D, 1.8D, 0.0D);
        this.endBeacon = endArmorStand;

        this.direction = this.endRails.clone().add(0.5D, 0.0D, 0.5D).subtract(0.0D, 0.75D, 0.0D).subtract(this.startRails.clone().add(0.5D, 0.0D, 0.5D)).toVector().normalize();

        this.startBeacon = startPlatform.getWorld().spawn(startPlatform, ArmorStand.class);
        this.startBeacon.setVisible(false);
        this.startBeacon.setGravity(false);

        this.hub.getTaskManager().getCirclesTask().addCircleAt(startPlatform);

        this.startTask = ProximityUtils.onNearbyOf(hub, this.startBeacon, 0.5D, 0.5D, 0.5D, Player.class, this::play);
    }

    @Override
    public void onDisable()
    {
        this.travellingTasks.values().forEach(BukkitTask::cancel);
        this.vehicles.values().forEach(EntityYodel::die);

        this.travellingTasks.clear();
        this.vehicles.clear();

        this.startTask.cancel();
        this.startBeacon.remove();
        this.endBeacon.remove();
    }

    @Override
    public void play(Player player)
    {
        if (this.travellingTasks.containsKey(player.getUniqueId()))
            return;

        this.hub.getServer().getScheduler().runTask(this.hub, () ->
        {
            WorldServer world = ((CraftWorld) this.startRails.getWorld()).getHandle();
            EntityYodel yodelEntity = new EntityYodel(world);
            yodelEntity.setPosition(this.startRails.getX(), this.startRails.getY(), this.startRails.getZ());
            world.addEntity(yodelEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);

            yodelEntity.getBukkitEntity().setPassenger(player);
            ((Fireball) yodelEntity.getBukkitEntity()).setDirection(this.direction);
            yodelEntity.getBukkitEntity().setVelocity(this.direction);

            this.vehicles.put(player.getUniqueId(), yodelEntity);

            this.travellingTasks.put(player.getUniqueId(), ProximityUtils.onNearbyOf(this.hub, this.endBeacon, 0.5D, 0.5D, 0.5D, Player.class, (p) ->
            {
                if (p.getUniqueId().equals(player.getUniqueId()))
                    this.arrived(player, yodelEntity);
            }));
        });
    }

    @Override
    public void stop(Player player)
    {
        if (this.travellingTasks.containsKey(player.getUniqueId()))
        {
            this.travellingTasks.get(player.getUniqueId()).cancel();
            this.travellingTasks.remove(player.getUniqueId());
        }

        if (this.vehicles.containsKey(player.getUniqueId()))
        {
            this.vehicles.get(player.getUniqueId()).die();
            this.vehicles.remove(player.getUniqueId());
        }
    }

    @Override
    public boolean hasPlayer(Player player)
    {
        return this.travellingTasks.containsKey(player.getUniqueId()) && this.vehicles.containsKey(player.getUniqueId());
    }

    private void arrived(Player player, EntityYodel vehiclePlatform)
    {
        this.travellingTasks.get(player.getUniqueId()).cancel();
        this.travellingTasks.remove(player.getUniqueId());

        vehiclePlatform.getBukkitEntity().eject();
        vehiclePlatform.die();

        this.vehicles.remove(player.getUniqueId());

        player.teleport(this.endPlatform);
    }
}
