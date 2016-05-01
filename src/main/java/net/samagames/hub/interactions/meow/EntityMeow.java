package net.samagames.hub.interactions.meow;

import net.minecraft.server.v1_9_R1.*;

import java.util.UUID;

class EntityMeow extends EntityOcelot
{
    private static final UUID OWNER_UUID = UUID.fromString("29b2b527-1b59-45df-b7b0-d5ab20d8731a");

    EntityMeow(World world)
    {
        super(world);

        this.setCatType(2);
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
    protected void da() {}

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
