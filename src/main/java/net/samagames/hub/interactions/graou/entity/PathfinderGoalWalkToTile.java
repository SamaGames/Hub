package net.samagames.hub.interactions.graou.entity;

import net.minecraft.server.v1_10_R1.EntityCreature;
import net.minecraft.server.v1_10_R1.PathfinderGoal;

public class PathfinderGoalWalkToTile extends PathfinderGoal
{
    private final EntityCreature entitycreature;
    private final float speed;
    private double x, y, z;
    private boolean canceled;

    PathfinderGoalWalkToTile(EntityCreature entitycreature, float speed)
    {
        this.speed = speed;
        this.entitycreature = entitycreature;
    }

    public void setTileToWalk(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;

        this.canceled = false;
    }

    public void cancel()
    {
        this.canceled = true;
    }

    @Override
    public boolean a()
    {
        return !this.canceled;
    }

    @Override
    public void e()
    {
        this.entitycreature.getNavigation().a(this.x, this.y, this.z, this.speed);
    }
}
