package net.samagames.hub.cosmetics.gadgets.displayers;

import net.minecraft.server.v1_8_R3.*;
import net.samagames.hub.Hub;
import net.samagames.hub.utils.SimpleBlock;
import net.samagames.tools.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.Random;


public class AnimalChestDisplayer extends AbstractDisplayer
{
	private Location centerLoc;
	private EntityType[] TYPES = new EntityType[]{EntityType.CAVE_SPIDER, EntityType.COW, EntityType.CREEPER, EntityType.ENDERMAN, EntityType.ENDERMITE,
			EntityType.GUARDIAN, EntityType.IRON_GOLEM, EntityType.MAGMA_CUBE, EntityType.OCELOT, EntityType.PIG, EntityType.PIG_ZOMBIE,
			EntityType.RABBIT, EntityType.SHEEP, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.SLIME, EntityType.SNOWMAN, EntityType.SPIDER,
			EntityType.SQUID, EntityType.VILLAGER, EntityType.WITCH, EntityType.WOLF, EntityType.ZOMBIE};
	private EntityType type;
	
	public AnimalChestDisplayer(Player player)
	{
		super(player);
		
		this.baseLocation = player.getLocation().add(1, 0, 1);
		this.addBlockToUse(this.baseLocation, new SimpleBlock(Material.CHEST, (byte)4));
		
		this.centerLoc = this.baseLocation.clone().add(0.5D, 0.5D, 0.5D);
		Random r = new Random();
		this.type = this.TYPES[r.nextInt(this.TYPES.length)];
	}

	@SuppressWarnings("deprecation")
	@Override
	public void display()
	{
		for (Location block : this.blocksUsed.keySet())
		{
            block.getBlock().setType(this.blocksUsed.get(block).getType());
            block.getBlock().setData(this.blocksUsed.get(block).getData());
        }
		
		BukkitTask particletask = Bukkit.getScheduler().runTaskTimer(Hub.getInstance(), () ->
		{
            Random random = new Random();
            double theta = 2 * Math.PI * random.nextDouble();
            double phi = Math.acos(2 * random.nextDouble() - 1);
            double x = this.centerLoc.getX() + (1 * Math.sin(phi) * Math.cos(theta));
            double y = this.centerLoc.getY() + (1 * Math.sin(phi) * Math.sin(theta));
            double z = this.centerLoc.getZ() + (1 * Math.cos(phi));
            ParticleEffect.PORTAL.display(new Vector(x, y, z), 0.1F, this.centerLoc.clone().add(new Vector(x, y, z).multiply(0.9)), 160.0D);
        }, 2, 2);

		Bukkit.getScheduler().runTaskLater(Hub.getInstance(), () ->
		{
            BlockPosition position = new BlockPosition(this.baseLocation.getX(), this.baseLocation.getY(), this.baseLocation.getZ());
            Block block = ((CraftWorld) this.baseLocation.getWorld()).getHandle().c(position);
            PacketPlayOutBlockAction openingpacket = new PacketPlayOutBlockAction(position, block, 1, 1);

            for (Player p : Bukkit.getOnlinePlayers())
                ((CraftPlayer)p).getHandle().playerConnection.sendPacket(openingpacket);

            BukkitTask spawningtask = Bukkit.getScheduler().runTaskTimer(Hub.getInstance(), () ->
			{
                Entity e = this.centerLoc.getWorld().spawnEntity(this.centerLoc.clone().add(0, 0.3, 0), this.type);
                e.teleport(this.centerLoc);
				this.freeze(e);
                double x = Math.random() * 2 - 1;
                double y = Math.random();
                double z = Math.random() * 2 - 1;
                e.setVelocity(new Vector(x, y, z).multiply(0.9));

                Bukkit.getScheduler().runTaskLater(Hub.getInstance(), e::remove, 20);
            }, 10, 2);

            Bukkit.getScheduler().runTaskLater(Hub.getInstance(), () ->
			{
                particletask.cancel();
                spawningtask.cancel();
				this.restore();
                this.end();
            }, 200);
        }, 100);
	}

	@Override
	public void handleInteraction(Entity who, Entity with){}

	@Override
	public boolean canUse()
	{
		return true;
	}
	
	private void freeze(Entity e)
	{
		net.minecraft.server.v1_8_R3.Entity entity = ((CraftEntity)e).getHandle();
		if (!(entity instanceof EntityInsentient))
			return ;
		EntityInsentient ce = (EntityInsentient)entity;
		Field bField;

        try
        {
            bField = PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);

            bField.set(ce.goalSelector, new UnsafeList<PathfinderGoalSelector>());
            bField.set(ce.targetSelector, new UnsafeList<PathfinderGoalSelector>());
            cField.set(ce.goalSelector, new UnsafeList<PathfinderGoalSelector>());
            cField.set(ce.targetSelector, new UnsafeList<PathfinderGoalSelector>());

            ((Navigation) ce.getNavigation()).a(true);
        }
        catch (Exception ignored) {}
	}
}
