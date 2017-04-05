package net.samagames.hub.cosmetics.gadgets.displayers;

import com.google.common.collect.Sets;
import net.minecraft.server.v1_9_R2.*;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.gadgets.GadgetManager;
import net.samagames.hub.utils.EntityUtils;
import net.samagames.tools.ParticleEffect;
import net.samagames.tools.SimpleBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;

public class AnimalChestDisplayer extends AbstractDisplayer
{
    private static final EntityType[] TYPES = new EntityType[] {
            EntityType.CAVE_SPIDER, EntityType.COW, EntityType.CREEPER, EntityType.ENDERMAN, EntityType.ENDERMITE,
            EntityType.GUARDIAN, EntityType.IRON_GOLEM, EntityType.MAGMA_CUBE, EntityType.OCELOT, EntityType.PIG,
            EntityType.PIG_ZOMBIE, EntityType.RABBIT, EntityType.SHEEP, EntityType.SILVERFISH, EntityType.SKELETON,
            EntityType.SLIME, EntityType.SNOWMAN, EntityType.SPIDER, EntityType.SQUID, EntityType.VILLAGER,
            EntityType.WITCH, EntityType.WOLF, EntityType.ZOMBIE, EntityType.HORSE, EntityType.MUSHROOM_COW,
            EntityType.SHULKER, EntityType.WITHER_SKULL
    };

    private final Location centerLoc;
    private final EntityType type;

    public AnimalChestDisplayer(Hub hub, Player player)
    {
        super(hub, player);

        this.baseLocation = player.getLocation().add(1, 0, 1);
        this.addBlockToUse(this.baseLocation, new SimpleBlock(Material.CHEST, (byte)4));

        this.centerLoc = this.baseLocation.clone().add(-0.5D, 0.75D, -0.5D);
        this.type = TYPES[GadgetManager.RANDOM.nextInt(TYPES.length)];
    }

    @Override
    public void display()
    {
        for (Location block : this.blocksUsed.keySet())
        {
            block.getBlock().setType(this.blocksUsed.get(block).getType());
            block.getBlock().setData(this.blocksUsed.get(block).getData());
        }

        BukkitTask particleTask = this.hub.getServer().getScheduler().runTaskTimer(this.hub, () ->
        {
            double theta = 2 * Math.PI * GadgetManager.RANDOM.nextDouble();
            double phi = Math.acos(2 * GadgetManager.RANDOM.nextDouble() - 1);
            double x = this.centerLoc.getX() + (1 * Math.sin(phi) * Math.cos(theta));
            double y = this.centerLoc.getY() + (1 * Math.sin(phi) * Math.sin(theta));
            double z = this.centerLoc.getZ() + (1 * Math.cos(phi));

            ParticleEffect.PORTAL.display(new Vector(x, y, z), 0.1F, this.centerLoc.clone().add(new Vector(x, y, z).multiply(0.9)), 160.0D);
        }, 2, 2);

        this.hub.getServer().getScheduler().runTaskLater(this.hub, () ->
        {
            BlockPosition position = new BlockPosition(this.baseLocation.getX(), this.baseLocation.getY(), this.baseLocation.getZ());
            Block block = ((CraftWorld) this.baseLocation.getWorld()).getHandle().c(position).getBlock();
            PacketPlayOutBlockAction openingpacket = new PacketPlayOutBlockAction(position, block, 1, 1);

            for (Player p : this.hub.getServer().getOnlinePlayers())
                ((CraftPlayer)p).getHandle().playerConnection.sendPacket(openingpacket);

            BukkitTask spawningTask = this.hub.getServer().getScheduler().runTaskTimer(this.hub, () ->
            {
                Entity entity = this.centerLoc.getWorld().spawnEntity(this.centerLoc.clone().add(0, 0.3, 0), this.type);
                entity.teleport(this.centerLoc);

                EntityUtils.freezeEntity(entity);

                double x = Math.random() * 2 - 1;
                double y = Math.random();
                double z = Math.random() * 2 - 1;

                entity.setVelocity(new Vector(x, y, z).multiply(0.9));

                this.hub.getServer().getScheduler().runTaskLater(this.hub, entity::remove, 20);
            }, 10, 2);

            this.hub.getServer().getScheduler().runTaskLater(this.hub, () ->
            {
                particleTask.cancel();
                spawningTask.cancel();

                this.restore();
                this.end();
            }, 20L * 10);
        }, 20L * 5);
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
