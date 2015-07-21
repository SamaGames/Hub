package net.samagames.hub.cosmetics.pets.nms;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;

import java.lang.reflect.Field;

public class CosmeticSlime extends EntitySlime
{
    public CosmeticSlime(World world)
    {
        super(world);

        Field bField;

        try
        {
            bField = PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);

            bField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
            bField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
            cField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
            cField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());

            ((Navigation) getNavigation()).a(true);
        }
        catch (Exception ignored) {}
    }

    @Override
    public void h()
    {
        super.h();
    }

    @Override
    public void g(float sideMot, float forMot)
    {
        try
        {
            if (this.passenger == null || !(this.passenger instanceof EntityHuman))
            {
                super.g(sideMot, forMot);
                this.S = 0.5F;
                return;
            }

            this.S = 1.0F;

            this.lastYaw = this.yaw = this.passenger.yaw;
            this.pitch = this.passenger.pitch * 0.5F;
            this.setYawPitch(this.yaw, this.pitch);
            this.aI = this.aG = this.yaw;

            sideMot = ((EntityLiving) this.passenger).aZ * 0.5F;
            forMot = ((EntityLiving) this.passenger).ba;

            if (forMot <= 0.0F)
                forMot *= 0.25F;

            sideMot *= 0.75F;

            this.k(0.25F);
            super.g(sideMot, forMot);

            Field jumpField = EntityLiving.class.getDeclaredField("aY");
            jumpField.setAccessible(true);
            boolean jump = jumpField.getBoolean(passenger);

            if (jump && this.onGround)
                this.motY = 0.5D;
        }
        catch (NoSuchFieldException | IllegalAccessException | SecurityException ignored) {}
    }

    @Override
    public boolean isPersistent()
    {
        return true;
    }

    @Override
    public void b(NBTTagCompound nbttagcompound) {}

    @Override
    public boolean c(NBTTagCompound nbttagcompound) {
        return false;
    }

    @Override
    public void a(NBTTagCompound nbttagcompound) {}

    @Override
    public boolean d(NBTTagCompound nbttagcompound) {
        return false;
    }

    @Override
    public void e(NBTTagCompound nbttagcompound) {}
}
