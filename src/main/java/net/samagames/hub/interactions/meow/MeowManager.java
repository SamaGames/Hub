package net.samagames.hub.interactions.meow;

import com.google.gson.JsonArray;
import net.minecraft.server.v1_9_R1.EntityOcelot;
import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteractionManager;
import net.samagames.tools.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.logging.Level;

public class MeowManager extends AbstractInteractionManager<Meow> implements Listener
{
    public MeowManager(Hub hub)
    {
        super(hub, "meow");

        this.hub.getEntityManager().registerEntity("Meow", 98, EntityOcelot.class, EntityMeow.class);
    }

    @Override
    public void onLogin(Player player)
    {
        super.onLogin(player);

        this.interactions.forEach(meow -> meow.onLogin(player));
    }

    @Override
    public void onLogout(Player player)
    {
        super.onLogout(player);

        this.interactions.forEach(meow -> meow.onLogout(player));
    }

    public void update(Player player)
    {
        this.interactions.forEach(meow -> meow.update(player));
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
    {
        if (event.getDamager().getType() == EntityType.PLAYER && event.getEntity().getType() == EntityType.OCELOT)
        {
            for (Meow meow : this.interactions)
            {
                if (meow.getMeowEntity().getBukkitEntity().getUniqueId().equals(event.getEntity().getUniqueId()))
                {
                    meow.play((Player) event.getDamager());
                    break;
                }
            }
        }
    }

    @Override
    public void loadConfiguration(JsonArray rootJson)
    {
        for (int i = 0; i < rootJson.size(); i++)
        {
            Location location = LocationUtils.str2loc(rootJson.get(i).getAsString());
            Meow meow = new Meow(this.hub, location);

            this.interactions.add(meow);
            this.log(Level.INFO, "Registered Meow at '" + rootJson.get(i).getAsString());
        }
    }
}
