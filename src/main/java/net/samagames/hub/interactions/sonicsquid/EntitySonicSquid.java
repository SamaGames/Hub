package net.samagames.hub.interactions.sonicsquid;

import com.google.common.collect.Sets;
import net.minecraft.server.v1_9_R1.*;
import net.samagames.hub.Hub;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_9_R1.util.UnsafeList;
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

        Field bField;

        try
        {
            bField = PathfinderGoalSelector.class.getDeclaredField("b");
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
        this.motX = -Math.sin(entityliving.yaw * Math.PI / 180) * 1.025F;
        this.motY = 0;
        this.motZ = Math.cos(entityliving.yaw * Math.PI / 180) * 1.025F;
        this.velocityChanged = true;
        this.move(this.motX, this.motY, this.motZ);

        // TODO: A working shit
    }

    @Override
    public void move(double d0, double d1, double d2)
    {
        super.move(d0, d1, d2);

        Block block = this.getFacingBlock();

        if (block != null && !block.isLiquid())
        {
            this.getBukkitEntity().eject();

            if (this.hub.getServer().getPlayer(this.uuid).isOnline())
                this.hub.getServer().getPlayer(this.uuid).teleport(block.getLocation().add(0.0D, 1.0D, 0.0D));

            this.die();
        }
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

    private Block getFacingBlock()
    {
        double rotation = (this.getBukkitEntity().getLocation().getYaw() - 90) % 360;

        if (rotation < 0)
            rotation += 360.0;

        Block block = null;

        if (0 <= rotation && rotation < 22.5)
            block = this.getBukkitEntity().getLocation().add(0.0D, 0.0D, -1.0D).getBlock();
        else if (22.5 <= rotation && rotation < 67.5)
            block = this.getBukkitEntity().getLocation().add(1.0D, 0.0D, -1.0D).getBlock();
        else if (67.5 <= rotation && rotation < 112.5)
            block = this.getBukkitEntity().getLocation().add(1.0D, 0.0D, 0.0D).getBlock();
        else if (112.5 <= rotation && rotation < 157.5)
            block = this.getBukkitEntity().getLocation().add(1.0D, 0.0D, 1.0D).getBlock();
        else if (157.5 <= rotation && rotation < 202.5)
            block = this.getBukkitEntity().getLocation().add(0.0D, 0.0D, 1.0D).getBlock();
        else if (202.5 <= rotation && rotation < 247.5)
            block = this.getBukkitEntity().getLocation().add(-1.0D, 0.0D, 1.0D).getBlock();
        else if (247.5 <= rotation && rotation < 292.5)
            block = this.getBukkitEntity().getLocation().add(-1.0D, 0.0D, 0.0D).getBlock();
        else if (292.5 <= rotation && rotation < 337.5)
            block = this.getBukkitEntity().getLocation().add(-1.0D, 0.0D, -1.0D).getBlock();
        else if (337.5 <= rotation && rotation < 360.0)
            block = this.getBukkitEntity().getLocation().add(0.0D, 0.0D, -1.0D).getBlock();

        return block;
    }
}
