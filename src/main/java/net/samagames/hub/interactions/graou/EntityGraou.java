package net.samagames.hub.interactions.graou;

import net.minecraft.server.v1_10_R1.*;

import java.util.UUID;

class EntityGraou extends EntityOcelot
{
    private static final UUID OWNER_UUID = UUID.fromString("29b2b527-1b59-45df-b7b0-d5ab20d8731a");

    EntityGraou(World world)
    {
        super(world);

        this.setCatType(3);
    }

    public void postInit(float yaw, float pitch)
    {
        super.setYawPitch(yaw, pitch);

        this.setTamed(true);
        this.setOwnerUUID(OWNER_UUID);
        this.goalSit.setSitting(true);
        this.persistent = true;
    }

    @Override
    protected void df() {}

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
    public void f(NBTTagCompound nbttagcompound) {}

    @Override
    protected SoundEffect G()
    {
        return SoundEffects.T;
    }
}
