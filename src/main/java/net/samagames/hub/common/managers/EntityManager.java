package net.samagames.hub.common.managers;

import net.minecraft.server.v1_9_R2.*;
import net.samagames.hub.Hub;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@SuppressWarnings("unchecked")
public class EntityManager extends AbstractManager
{
    private final BiomeBase[] biomes;

    public EntityManager(Hub hub)
    {
        super(hub);

        this.biomes = new BiomeBase[BiomeBase.REGISTRY_ID.keySet().size()];

        int i = 0;

        for (MinecraftKey key : BiomeBase.REGISTRY_ID.keySet())
        {
            this.biomes[i] = BiomeBase.REGISTRY_ID.get(key);
            i++;
        }
    }

    @Override
    public void onDisable() { /** Not needed **/ }

    @Override
    public void onLogin(Player player) { /** Not needed **/ }

    @Override
    public void onLogout(Player player) { /** Not needed **/ }

    public void registerEntity(String name, int id, Class<? extends Entity> nmsClass, Class<? extends Entity> customClass)
    {
        try
        {
            this.registerEntityInEntityEnum(customClass, name, id);
        }
        catch (Exception e)
        {
            this.log(Level.SEVERE, "Can't register custom entity '" + customClass.getName() + "'!");
            e.printStackTrace();

            return;
        }

        if (EntityInsentient.class.isAssignableFrom(nmsClass) && EntityInsentient.class.isAssignableFrom(customClass))
        {
            for (BiomeBase biomeBase : this.biomes)
            {
                if (biomeBase == null)
                    break;

                for (String field : new String[]{"u", "v", "w", "x"})
                {
                    try
                    {
                        Field list = BiomeBase.class.getDeclaredField(field);
                        list.setAccessible(true);
                        List<BiomeBase.BiomeMeta> mobList = (List<BiomeBase.BiomeMeta>) list.get(biomeBase);

                        mobList.stream().filter(meta -> nmsClass.equals(meta.b)).forEach(meta -> meta.b = (Class<? extends EntityInsentient>) customClass);
                    }
                    catch (Exception e)
                    {
                        this.log(Level.SEVERE, "Can't register custom entity '" + customClass.getName() + "'!");
                        e.printStackTrace();
                    }
                }
            }
        }

        this.log(Level.INFO, "Registered custom entity '" + customClass.getName() + "'");
    }

    private void registerEntityInEntityEnum(Class<? extends Entity> paramClass, String paramString, int paramInt) throws Exception
    {
        ((Map<String, Class<? extends Entity>>) this.getPrivateStatic(EntityTypes.class, "c")).put(paramString, paramClass);
        ((Map<Class<? extends Entity>, String>) this.getPrivateStatic(EntityTypes.class, "d")).put(paramClass, paramString);
        ((Map<Integer, Class<? extends Entity>>) this.getPrivateStatic(EntityTypes.class, "e")).put(paramInt, paramClass);
        ((Map<Class<? extends Entity>, Integer>) this.getPrivateStatic(EntityTypes.class, "f")).put(paramClass, paramInt);
        ((Map<String, Integer>) this.getPrivateStatic(EntityTypes.class, "g")).put(paramString, paramInt);
    }

    private Object getPrivateStatic(Class clazz, String f) throws Exception
    {
        Field field = clazz.getDeclaredField(f);
        field.setAccessible(true);

        return field.get(null);
    }
}
