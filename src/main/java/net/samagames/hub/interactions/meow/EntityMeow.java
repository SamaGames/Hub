package net.samagames.hub.interactions.meow;

import net.minecraft.server.v1_9_R1.*;

class EntityMeow extends EntityOcelot
{
    EntityMeow(World world)
    {
        super(world);
        this.setCatType(2);
    }

    public void setYawPitch(float yaw, float pitch)
    {
        super.setYawPitch(yaw, pitch);
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
