package net.samagames.hub.interactions.sonicsquid;

import com.google.common.collect.Sets;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

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
class EntitySonicSquid extends EntitySquid
{
    EntitySonicSquid(World world, Player player)
    {
        super(world);

        try
        {
            Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);

            bField.set(this.goalSelector, Sets.newLinkedHashSet());
            bField.set(this.targetSelector, Sets.newLinkedHashSet());
            cField.set(this.goalSelector, Sets.newLinkedHashSet());
            cField.set(this.targetSelector, Sets.newLinkedHashSet());

            ((Navigation) getNavigation()).a(true);
        }
        catch (ReflectiveOperationException ignored) {}

        Location copy = player.getLocation().clone();

        while (copy.getBlock().getType() == Material.WATER || copy.getBlock().getType() == Material.STATIONARY_WATER)
            copy.add(0.0D, 1.0D, 0.0D);

        this.setPosition(player.getLocation().getX(), copy.getY() - 1.35D, player.getLocation().getZ());
    }

    @Override
    protected void r()
    {
        /** Disable AI **/
    }

    @Override
    public void n()
    {
        EntityLiving entityliving = (EntityLiving) this.bG();

        if (entityliving == null)
        {
            for (Entity e : this.passengers)
            {
                if (e instanceof EntityHuman)
                {
                    entityliving = (EntityLiving) e;
                    break;
                }
            }

            if (entityliving == null)
            {
                this.die();
                return;
            }
        }

        /**Location location = new Location(entityliving.getBukkitEntity().getWorld(), 0, 0, 0, entityliving.yaw, 0);
        this.getBukkitEntity().setVelocity(location.getDirection().multiply(1.025F).normalize());
        this.getBukkitEntity().setVelocity(((Player) entityliving.getBukkitEntity()).getEyeLocation().getDirection().multiply(1.025F));**/

        this.setYawPitch(entityliving.yaw, 0.0F);
        this.motX = -Math.sin(entityliving.yaw * Math.PI / 180) * 0.7F;

        this.motY = 0;
        this.motZ = Math.cos(entityliving.yaw * Math.PI / 180) * 0.7F;
        this.velocityChanged = true;
        this.yaw = entityliving.yaw;
        this.positionChanged = true;
        this.move(EnumMoveType.SELF, this.motX, this.motY, this.motZ);
    }

    @Override
    public void move(EnumMoveType type, double d0, double d1, double d2)
    {
        super.move(type, d0, d1, d2);

        if (this.checkWater()
                || (Math.abs(this.locX) % 1 > 0.5D && this.checkBlock(this.locX > 0 ? this.locX + 0.5D : this.locX - 0.5D, this.locY, this.locZ))
                || (Math.abs(this.locX) % 1 < 0.5D && this.checkBlock(this.locX > 0 ? this.locX - 0.5D : this.locX + 0.5D, this.locY, this.locZ))
                || (Math.abs(this.locZ) % 1 > 0.5D && this.checkBlock(this.locX, this.locY, this.locZ > 0 ? this.locZ + 0.5D : this.locZ - 0.5D)))
            return ;
        if (Math.abs(this.locZ) % 1 < 0.5D)
            this.checkBlock(this.locX, this.locY, this.locZ > 0 ? this.locZ - 0.5D : this.locZ + 0.5D);
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

    private boolean checkBlock(double x, double y, double z)
    {
        net.minecraft.server.v1_12_R1.Material material = this.getWorld().getType(new BlockPosition(x, y, z)).getMaterial();

        if (material != net.minecraft.server.v1_12_R1.Material.AIR && material != net.minecraft.server.v1_12_R1.Material.WATER)
        {
            Location blockLocation = new Location(this.getWorld().getWorld(), x, y, z);
            this.destroySquid(blockLocation.getBlockX() + 0.5, y, blockLocation.getBlockZ() + 0.5D);
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean checkWater()
    {
        boolean noWater = true;
        for (int x = -1; x <= 1; x++)
            for (int y = -1; y <= 1; y++)
                for (int z = -1; z <= 1; z++)
                    noWater = noWater && (this.getWorld().getType(new BlockPosition(x + this.locX, y + this.locY, z + this.locZ)).getMaterial() != net.minecraft.server.v1_12_R1.Material.WATER);
        if (noWater)
            this.destroySquid(this.locX, this.locY, this.locZ);
        return noWater;
    }

    private void destroySquid(double x, double y, double z)
    {
        this.die();

        this.passengers.forEach(passenger ->
        {
            passenger.getBukkitEntity().eject();
            this.findBetterLandingLocation(passenger, x, y, z, passenger.yaw, passenger.pitch);
            passenger.getBukkitEntity().getWorld().playSound(passenger.getBukkitEntity().getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1F, 1F);
        });
    }

    private void findBetterLandingLocation(Entity passenger, double x, double y, double z, float yaw, float pitch)
    {
        while (y < 256)
        {
            if (this.getWorld().getType(new BlockPosition(x, y, z)).getMaterial() == net.minecraft.server.v1_12_R1.Material.AIR)
            {
                passenger.setLocation(x, y, z, yaw, pitch);
                return ;
            }
            y++;
        }
    }
}
