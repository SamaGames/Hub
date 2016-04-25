package net.samagames.hub.interactions.meow;

import com.google.common.collect.Sets;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.Location;

import java.lang.reflect.Field;

class EntityMeow extends EntityOcelot
{
    EntityMeow(World world, Location location)
    {
        super(world);

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

        this.setCatType(2);
        this.setSitting(true);

        this.setPosition(location.getX(), location.getY(), location.getZ());
        this.setYawPitch(location.getYaw(), location.getPitch());
    }

    @Override
    protected void r()
    {
        /** Disable AI **/
    }

    @Override
    public void g(float var1, float var2)
    {
        /** Disable moving **/
    }

    @Override
    public void e(float var1, float var2)
    {
        /** Disable moving **/
    }

    @Override
    public void move(double d0, double d1, double d2)
    {
        /** Disable moving **/
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
}
