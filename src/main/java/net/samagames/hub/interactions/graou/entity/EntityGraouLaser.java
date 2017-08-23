package net.samagames.hub.interactions.graou.entity;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.event.entity.EntityTargetEvent;

import java.lang.reflect.Field;

/*
 * This file is part of Hub.
 *
 * Hub is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hub is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hub.  If not, see <http://www.gnu.org/licenses/>.
 */
public class EntityGraouLaser extends EntityGuardian
{
    private static Field datawatcherField;

    public EntityGraouLaser(World world)
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
        this.goalSelector.a(4, new PathfinderGoalGuardianAttack(this));
        this.goalSelector.a(7, this.goalRandomStroll);
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
            return super.b();
        }

        public void c()
        {
            this.b = -10;
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
            EntityLiving target = this.entity.getGoalTarget();
            this.entity.getNavigation().p();
            this.entity.getControllerLook().a(target, 90.0F, 90.0F);

            this.entity.a(target.getId());

            super.e();
        }
    }

    static
    {
        try
        {
            datawatcherField = EntityGuardian.class.getDeclaredField("bB");
            datawatcherField.setAccessible(true);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
    }
}
