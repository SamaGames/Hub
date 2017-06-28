package net.samagames.hub.common.managers;

import net.minecraft.server.v1_12_R1.*;
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

    public <E extends Entity> void registerEntity(String name, int id, Class<? extends E> nmsClass, Class<? extends E> customClass)
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

                for (String field : new String[]{"t", "u", "v", "w"})
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

    private void registerEntityInEntityEnum(Class<? extends Entity> customClass, String name, int id) throws Exception
    {
        MinecraftKey key = new MinecraftKey(name);
        EntityTypes.b.a(id, key, customClass);

        if (!EntityTypes.d.contains(key))
            EntityTypes.d.add(key);
    }
}
