package net.samagames.hub.common.managers;

import net.minecraft.server.v1_8_R1.*;
import net.samagames.hub.Hub;
import net.samagames.hub.npcs.CustomEntityVillager;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class EntityManager extends AbstractManager
{
    public EntityManager(Hub hub)
    {
        super(hub);

        this.registerCustomEntities();
    }

    public void registerCustomEntities()
    {
        Hub.getInstance().log(this, Level.INFO, "Registering custom entities...");

        this.registerEntity("NPC", 120, EntityVillager.class, CustomEntityVillager.class);

        Hub.getInstance().log(this, Level.INFO, "Registered custom entites with success!");
    }

    public void registerEntity(String name, int id, Class<? extends Entity> nmsClass, Class<? extends Entity> customClass)
    {
        BiomeBase[] biomes;

        try
        {
            biomes = (BiomeBase[]) getPrivateStatic(BiomeBase.class, "biomes");
            this.registerEntityInEntityEnum(customClass, name, id);
        }
        catch (Exception e)
        {
            Hub.getInstance().log(this, Level.SEVERE, "Can't register custom entity '" + customClass.getName() + "'!");
            e.printStackTrace();

            return;
        }

        for (BiomeBase biomeBase : biomes)
        {
            if (biomeBase == null)
                break;

            for (String field : new String[]{"at", "au", "av", "aw"})
            {
                try
                {
                    Field list = BiomeBase.class.getDeclaredField(field);
                    list.setAccessible(true);
                    List<BiomeMeta> mobList = (List<BiomeMeta>) list.get(biomeBase);

                    for (BiomeMeta meta : mobList)
                        if(nmsClass.equals(meta.b))
                            meta.b = customClass;
                }
                catch (Exception e)
                {
                    Hub.getInstance().log(this, Level.SEVERE, "Can't register custom entity '" + customClass.getName() + "'!");
                    e.printStackTrace();
                }
            }
        }

        Hub.getInstance().log(this, Level.INFO, "Registered custom entity '" + customClass.getName() + "'");
    }

    private void registerEntityInEntityEnum(Class paramClass, String paramString, int paramInt) throws Exception
    {
        ((Map) this.getPrivateStatic(EntityTypes.class, "c")).put(paramString, paramClass);
        ((Map) this.getPrivateStatic(EntityTypes.class, "d")).put(paramClass, paramString);
        ((Map) this.getPrivateStatic(EntityTypes.class, "e")).put(paramInt, paramClass);
        ((Map) this.getPrivateStatic(EntityTypes.class, "f")).put(paramClass, paramInt);
        ((Map) this.getPrivateStatic(EntityTypes.class, "g")).put(paramString, paramInt);
    }

    private Object getPrivateStatic(Class clazz, String f) throws Exception
    {
        Field field = clazz.getDeclaredField(f);
        field.setAccessible(true);

        return field.get(null);
    }

    @Override
    public String getName() { return "EntityManager"; }
}
