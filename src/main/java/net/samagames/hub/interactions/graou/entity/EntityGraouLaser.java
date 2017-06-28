package net.samagames.hub.interactions.graou.entity;

import net.minecraft.server.v1_12_R1.*;

import java.lang.reflect.Field;

public class EntityGraouLaser extends EntityGuardian
{
    private static Field datawatcherField;

    public EntityGraouLaser(World world)
    {
        super(world);

        //this.setInvisible(true);
        this.setInvulnerable(true);
        this.setNoGravity(true);
    }

    @Override
    protected void r()
    {
        this.goalRandomStroll = new PathfinderGoalRandomStroll(this, 1.0D, 80);
        this.goalSelector.a(1, new PathfinderGoalGuardianAttack(this));
        this.goalSelector.a(2, this.goalRandomStroll);
        this.goalRandomStroll.a(3);
    }

    public void a(int var1)
    {
        try
        {
            this.datawatcher.set((DataWatcherObject<Integer>) datawatcherField.get(this), var1);
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
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
    public void f(NBTTagCompound nbttagcompound) {}

    @Override
    protected SoundEffect F()
    {
        return null;
    }

    static class PathfinderGoalGuardianAttack extends PathfinderGoal
    {
        private final EntityGraouLaser entity;
        private int b;

        public PathfinderGoalGuardianAttack(EntityGraouLaser entity)
        {
            this.entity = entity;
            this.a(3);
        }

        public boolean a()
        {
            EntityLiving var1 = this.entity.getGoalTarget();
            return var1 != null && var1.isAlive();
        }

        public boolean b()
        {
            return super.b() && this.entity.h(this.entity.getGoalTarget()) > 9.0D;
        }

        public void c()
        {
            this.entity.getNavigation().p();
            this.entity.getControllerLook().a(this.entity.getGoalTarget(), 90.0F, 90.0F);
            this.entity.impulse = true;
        }

        public void d()
        {
            this.entity.goalRandomStroll.i();
        }

        public void e()
        {
            EntityLiving var1 = this.entity.getGoalTarget();
            this.entity.getNavigation().p();
            this.entity.getControllerLook().a(var1, 90.0F, 90.0F);

            ++this.b;

            if (this.b == 0)
            {
                this.entity.a(this.entity.getGoalTarget().getId());
                this.entity.world.broadcastEntityEffect(this.entity, (byte) 21);
            }

            super.e();
        }
    }

    static
    {
        try
        {
            datawatcherField = EntityGuardian.class.getDeclaredField("b");
            datawatcherField.setAccessible(true);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
    }
}
