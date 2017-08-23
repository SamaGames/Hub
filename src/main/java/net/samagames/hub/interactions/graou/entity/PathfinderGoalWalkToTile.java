package net.samagames.hub.interactions.graou.entity;

import net.minecraft.server.v1_12_R1.EntityCreature;
import net.minecraft.server.v1_12_R1.PathfinderGoal;

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
