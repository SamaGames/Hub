package net.samagames.hub.cosmetics.pets;

import net.minecraft.server.v1_8_R2.EntityInsentient;
import net.minecraft.server.v1_8_R2.World;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class PetCosmetic<T extends LivingEntity> extends AbstractCosmetic
{
    private final Class<? extends EntityInsentient> entityClass;

    public PetCosmetic(String databaseName, String displayName, ItemStack icon, String[] description, Class<? extends EntityInsentient> entityClass)
    {
        super("pet." + databaseName, displayName, icon, description);
        this.entityClass = entityClass;
    }

    public abstract void applySettings(T spawned, String settings);

    public T spawn(Player player)
    {
        try
        {
            if (this.entityClass == null)
                return null;

            Constructor<?> ctor = this.entityClass.getConstructor(World.class);

            final World w = ((CraftWorld) player.getWorld()).getHandle();
            final EntityInsentient creature = (EntityInsentient) ctor.newInstance(w);

            Location l = player.getLocation();
            creature.setPosition(l.getX(), l.getY(), l.getZ());

            w.addEntity(creature, CreatureSpawnEvent.SpawnReason.CUSTOM);

            Bukkit.getScheduler().runTaskLater(Hub.getInstance(), () -> creature.getBukkitEntity().setPassenger(player), 10);

            return (T) creature.getBukkitEntity();

        }
        catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ignored) {}

        return null;
    }

    public void onUse(Player player) {}

    public Class<? extends EntityInsentient> getEntityClass()
    {
        return this.entityClass;
    }

    public boolean isOverridingUse()
    {
        return false;
    }
}
