package net.samagames.hub.interactions.graou;

import net.minecraft.server.v1_10_R1.*;

class EntityGraouLaser extends EntityGuardian
{
    EntityGraouLaser(World world)
    {
        super(world);

        this.setInvisible(true);
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
        return null;
    }

    static class PathfinderGoalGuardianAttack extends PathfinderGoal
    {
        private final EntityGuardian a;
        private int b;

        public PathfinderGoalGuardianAttack(EntityGuardian var1)
        {
            this.a = var1;
            this.a(3);
        }

        public boolean a()
        {
            EntityLiving var1 = this.a.getGoalTarget();
            return var1 != null && var1.isAlive();
        }

        public boolean b()
        {
            return super.b() && (this.a.isElder() || this.a.h(this.a.getGoalTarget()) > 9.0D);
        }

        public void c()
        {
            this.b = -10;
            this.a.getNavigation().o();
            this.a.getControllerLook().a(this.a.getGoalTarget(), 90.0F, 90.0F);
            this.a.impulse = true;
        }

        public void d()
        {
            this.a.goalRandomStroll.f();
        }

        public void e()
        {
            EntityLiving var1 = this.a.getGoalTarget();
            this.a.getNavigation().o();
            this.a.getControllerLook().a(var1, 90.0F, 90.0F);

            super.e();
        }
    }
}
