package net.samagames.hub.interactions.meow;

import net.minecraft.server.v1_12_R1.*;

import java.util.UUID;

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
    protected void dm() {}

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
}
