package net.samagames.hub.interactions.graou.entity;

import net.minecraft.server.v1_9_R2.EntitySquid;
import net.minecraft.server.v1_9_R2.NBTTagCompound;
import net.minecraft.server.v1_9_R2.SoundEffect;
import net.minecraft.server.v1_9_R2.World;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 25/10/2016
 */
public class EntityGraouLaserTarget extends EntitySquid
{
    public EntityGraouLaserTarget(World world)
    {
        super(world);

        this.setInvisible(true);
        this.setInvulnerable(true);
        //this.setNoGravity(true);
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
}
