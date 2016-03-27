package net.samagames.hub.interactions.yodels;

import net.minecraft.server.v1_9_R1.*;

class EntityYodel extends EntitySmallFireball
{
    EntityYodel(World world)
    {
        super(world);

        this.isIncendiary = false;
    }

    @Override
    protected void a(MovingObjectPosition movingobjectposition) {}

    @Override
    public void m()
    {
        this.motX *= 0.075D;
        this.motY *= 0.075D;
        this.motZ *= 0.075D;

        super.m();
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f)
    {
        return false;
    }

    @Override
    public void b(NBTTagCompound nbttagcompound) {}

    @Override
    public void a(NBTTagCompound nbttagcompound) {}

    @Override
    protected float l()
    {
        return 0.075F;
    }

    @Override
    public boolean isInteractable()
    {
        return false;
    }
}
