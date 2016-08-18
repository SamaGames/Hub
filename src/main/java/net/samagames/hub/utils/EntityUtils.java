package net.samagames.hub.utils;

import com.google.common.collect.Sets;
import net.minecraft.server.v1_9_R2.EntityInsentient;
import net.minecraft.server.v1_9_R2.Navigation;
import net.minecraft.server.v1_9_R2.PathfinderGoalSelector;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;

import java.lang.reflect.Field;

public class EntityUtils
{
    private EntityUtils()
    {
    }

    public static void freezeEntity(Entity e)
    {
        net.minecraft.server.v1_9_R2.Entity entity = ((CraftEntity)e).getHandle();

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
