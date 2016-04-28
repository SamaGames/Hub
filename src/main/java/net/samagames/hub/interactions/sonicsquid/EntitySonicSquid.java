package net.samagames.hub.interactions.sonicsquid;

import com.google.common.collect.Sets;
import net.minecraft.server.v1_9_R1.*;
import net.samagames.hub.Hub;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.UUID;

class EntitySonicSquid extends EntitySquid
{
    private final Hub hub;
    private final UUID uuid;

    EntitySonicSquid(Hub hub, World world, Player player)
    {
        super(world);

        this.hub = hub;
        this.uuid = player.getUniqueId();

        try
        {
            Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);

            bField.set(this.goalSelector, Sets.newLinkedHashSet());
            bField.set(this.targetSelector, Sets.newLinkedHashSet());
            cField.set(this.goalSelector, Sets.newLinkedHashSet());
            cField.set(this.targetSelector, Sets.newLinkedHashSet());

            ((Navigation) getNavigation()).a(true);
        }
        catch (ReflectiveOperationException ignored) {}

        Location copy = player.getLocation().clone();

        while (copy.getBlock().getType() == Material.WATER || copy.getBlock().getType() == Material.STATIONARY_WATER)
            copy.add(0.0D, 1.0D, 0.0D);

        this.setPosition(player.getLocation().getX(), copy.getY() - 0.85D, player.getLocation().getZ());
    }

    @Override
    protected void r()
    {
        /** Disable AI **/
    }

    @Override
    public void n()
    {
        EntityLiving entityliving = (EntityLiving) this.bt();

        if (entityliving == null)
        {
            for (Entity e : this.passengers)
            {
                if (e instanceof EntityHuman)
                {
                    entityliving = (EntityLiving) e;
                    break;
                }
            }

            if (entityliving == null)
            {
                this.die();
                return;
            }
        }
        /**Location location = new Location(entityliving.getBukkitEntity().getWorld(), 0, 0, 0, entityliving.yaw, 0);
        this.getBukkitEntity().setVelocity(location.getDirection().multiply(1.025F).normalize());
        this.getBukkitEntity().setVelocity(((Player) entityliving.getBukkitEntity()).getEyeLocation().getDirection().multiply(1.025F));**/

        this.setYawPitch(entityliving.yaw, 0.0F);
        this.motX = -Math.sin(entityliving.yaw * Math.PI / 180) * 0.7F;

        this.motY = 0;
        this.motZ = Math.cos(entityliving.yaw * Math.PI / 180) * 0.7F;
        this.velocityChanged = true;
        this.move(this.motX, this.motY, this.motZ);
    }

    @Override
    public void move(double d0, double d1, double d2)
    {
        super.move(d0, d1, d2);

        if (this.checkWater()
                || (this.locX - (long)locX > 0.7D && this.checkBlock(this.locX + 0.3D, this.locY, this.locZ))
                || (this.locX - (long)locX < 0.3D && this.checkBlock(this.locX - 0.3D, this.locY, this.locZ))
                || (this.locZ - (long)locZ > 0.7D && this.checkBlock(this.locZ + 0.3D, this.locY, this.locZ)))
            return ;
        if (this.locZ - (long)locZ > 0.7D)
            this.checkBlock(this.locZ + 0.3D, this.locY, this.locZ);
    }

    @Override
    public void b(NBTTagCompound nbttagcompound) {}

    @Override
    public boolean c(NBTTagCompound nbttagcompound)
    {
        return false;
    }

    @Override
    public void a(NBTTagCompound nbttagcompound) {}

    @Override
    public boolean d(NBTTagCompound nbttagcompound)
    {
        return false;
    }

    @Override
    public void e(NBTTagCompound nbttagcompound) {}

    private boolean checkBlock(double x, double y, double z)
    {
        net.minecraft.server.v1_9_R1.Material material = this.getWorld().getType(new BlockPosition(x, y, z)).getMaterial();

        if (material != net.minecraft.server.v1_9_R1.Material.AIR && material != net.minecraft.server.v1_9_R1.Material.WATER)
        {
            this.destroySquid(x, y, z);
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean checkWater()
    {
        boolean noWater = true;
        for (int x = -1; x <= 1; x++)
            for (int y = -1; y <= 1; y++)
                for (int z = -1; z <= 1; z++)
                    noWater = noWater && (this.getWorld().getType(new BlockPosition(x, y, z)).getMaterial() != net.minecraft.server.v1_9_R1.Material.WATER);
        if (noWater)
            this.destroySquid(this.locX, this.locY, this.locZ);
        return noWater;
    }

    private void destroySquid(double x, double y, double z)
    {
        this.die();

        this.passengers.forEach(passenger ->
        {
            passenger.getBukkitEntity().eject();
            this.findBetterLandingLocation(passenger, x + 0.5D, y, z + 0.5D, passenger.yaw, passenger.pitch);
        });
    }

    private void findBetterLandingLocation(Entity passenger, double x, double y, double z, float yaw, float pitch)
    {
        while (y < 256)
        {
            if (this.getWorld().getType(new BlockPosition(x, y, z)).getMaterial() == net.minecraft.server.v1_9_R1.Material.AIR)
            {
                passenger.setLocation(x, y, z, yaw, pitch);
                return ;
            }
            y++;
        }
    }
}
