package net.samagames.hub.cosmetics.gadgets.displayers;

import java.lang.reflect.Field;
import java.util.Random;

import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.Navigation;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockAction;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
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


public class AnimalChestDisplayer extends AbstractDisplayer
{
	private Location centerLoc;
	private EntityType[] TYPES = new EntityType[]{EntityType.CAVE_SPIDER, EntityType.COW, EntityType.CREEPER, EntityType.ENDERMAN, EntityType.ENDERMITE,
			EntityType.GUARDIAN, EntityType.HORSE, EntityType.IRON_GOLEM, EntityType.MAGMA_CUBE, EntityType.OCELOT, EntityType.PIG, EntityType.PIG_ZOMBIE,
			EntityType.RABBIT, EntityType.SHEEP, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.SLIME, EntityType.SNOWMAN, EntityType.SPIDER,
			EntityType.SQUID, EntityType.VILLAGER, EntityType.WITCH, EntityType.WITHER_SKULL, EntityType.WOLF, EntityType.ZOMBIE};
	private EntityType type;
	
	public AnimalChestDisplayer(Player player)
	{
		super(player);
		
		this.baseLocation = player.getLocation().add(1, 0, 1);
		this.addBlockToUse(this.baseLocation, new SimpleBlock(Material.CHEST, (byte)4));
		
		this.centerLoc = this.baseLocation.clone().add(0.5D, 0.5D, 0.5D);
		Random r = new Random();
		type = TYPES[r.nextInt(TYPES.length)];
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
		
		BukkitTask particletask = Bukkit.getScheduler().runTaskTimer(Hub.getInstance(), new Runnable()
		{
			@Override
			public void run()
			{
				Random random = new Random();
				double theta = 2 * Math.PI * random.nextDouble();
				double phi = Math.acos(2 * random.nextDouble() - 1);
				double x = centerLoc.getX() + (1 * Math.sin(phi) * Math.cos(theta));
				double y = centerLoc.getY() + (1 * Math.sin(phi) * Math.sin(theta));
				double z = centerLoc.getZ() + (1 * Math.cos(phi));
				ParticleEffect.PORTAL.display(new Vector(x, y, z), 0.1F, centerLoc.clone().add(new Vector(x, y, z).multiply(0.3)), 160.0D);
			}
		}, 2, 2);
		Bukkit.getScheduler().runTaskLater(Hub.getInstance(), new Runnable()
		{
			@Override
			public void run()
			{
				BlockPosition position = new BlockPosition(baseLocation.getX(), baseLocation.getY(), baseLocation.getZ());
				Block block = ((CraftWorld)baseLocation.getWorld()).getHandle().c(position);
				PacketPlayOutBlockAction openingpacket = new PacketPlayOutBlockAction(position, block, 1, 1);
				for (Player p : Bukkit.getOnlinePlayers())
					((CraftPlayer)p).getHandle().playerConnection.sendPacket(openingpacket);
				BukkitTask spawningtask = Bukkit.getScheduler().runTaskTimer(Hub.getInstance(), new Runnable()
				{
					@Override
					public void run()
					{
						Entity e = centerLoc.getWorld().spawnEntity(centerLoc.clone().add(0, 0.5, 0), type);
						freeze(e);
						double x = Math.random() * 2 - 1;
						double y = Math.random();
						double z = Math.random() * 2 - 1;
						e.setVelocity(new Vector(x, y, z).multiply(2));
						Bukkit.getScheduler().runTaskLater(Hub.getInstance(), new Runnable()
						{
							@Override
							public void run()
							{
								e.remove();
							}
						}, 20);
					}
				}, 20, 2);
				Bukkit.getScheduler().runTaskLater(Hub.getInstance(), new Runnable()
				{
					@Override
					public void run()
					{
						particletask.cancel();
						spawningtask.cancel();
						restore();
						end();
					}
				}, 200);
			}
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
