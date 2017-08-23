package net.samagames.hub.utils;

import com.google.common.collect.Sets;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.Navigation;
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;

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
public class EntityUtils
{
    private EntityUtils()
    {
    }

    public static void freezeEntity(Entity e)
    {
        net.minecraft.server.v1_12_R1.Entity entity = ((CraftEntity)e).getHandle();

        if (!(entity instanceof EntityInsentient))
            return ;

        EntityInsentient ce = (EntityInsentient) entity;
        Field bField;

        try
        {
            bField = PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);

            bField.set(ce.goalSelector, Sets.newLinkedHashSet());
            bField.set(ce.targetSelector, Sets.newLinkedHashSet());
            cField.set(ce.goalSelector, Sets.newLinkedHashSet());
            cField.set(ce.targetSelector, Sets.newLinkedHashSet());

            ((Navigation) ce.getNavigation()).a(true);
        }
        catch (ReflectiveOperationException ignored) {}
    }
}
