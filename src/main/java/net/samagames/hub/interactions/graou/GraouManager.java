package net.samagames.hub.interactions.graou;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.server.v1_10_R1.EntityGuardian;
import net.minecraft.server.v1_10_R1.EntityOcelot;
import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteractionManager;
import net.samagames.tools.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.*;
import java.util.logging.Level;

public class GraouManager extends AbstractInteractionManager<Graou> implements Listener
{
    private List<UUID> lock;

    public GraouManager(Hub hub)
    {
        super(hub, "graou");

        this.lock = new ArrayList<>();

        this.hub.getServer().getPluginManager().registerEvents(this, this.hub);
        this.hub.getEntityManager().registerEntity("Graou", 98, EntityOcelot.class, EntityGraou.class);
        this.hub.getEntityManager().registerEntity("GraouLaser", 68, EntityGuardian.class, EntityGraouLaser.class);
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

        if (this.lock.contains(player.getUniqueId()))
            this.lock.remove(player.getUniqueId());
    }

    public void update(Player player)
    {
        this.interactions.forEach(meow -> meow.update(player));
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
    {
        if (event.getRightClicked().getType() == EntityType.OCELOT)
        {
            if (this.lock.contains(event.getPlayer().getUniqueId()))
                return;

            // Event called twice
            this.lock.add(event.getPlayer().getUniqueId());
            this.hub.getServer().getScheduler().runTaskLater(this.hub, () -> this.lock.remove(event.getPlayer().getUniqueId()), 10L);

            for (Graou graou : this.interactions)
            {
                if (graou.getGraouEntity().getBukkitEntity().getUniqueId().equals(event.getRightClicked().getUniqueId()))
                {
                    graou.play(event.getPlayer());
                    break;
                }
            }
        }
    }

    @Override
    public void loadConfiguration(JsonArray rootJson)
    {
        if (rootJson.size() == 0)
            return;

        JsonObject graouJson = rootJson.get(0).getAsJsonObject();

        Location catLocation = LocationUtils.str2loc(graouJson.get("cat").getAsString());
        Location doorLocation = LocationUtils.str2loc(graouJson.get("door").getAsString());

        JsonArray treasureLocationsJson = graouJson.get("treasure").getAsJsonArray();
        Location[] treasureLocations = new Location[] {
                LocationUtils.str2loc(treasureLocationsJson.get(0).getAsString()),
                LocationUtils.str2loc(treasureLocationsJson.get(1).getAsString())
        };

        JsonArray openingLocationsJson = graouJson.get("opening").getAsJsonArray();
        Location[] openingLocations = new Location[] {
                LocationUtils.str2loc(openingLocationsJson.get(0).getAsString()),
                LocationUtils.str2loc(openingLocationsJson.get(1).getAsString())
        };

        Graou graou = new Graou(this.hub, catLocation, doorLocation, treasureLocations, openingLocations);

        this.interactions.add(graou);
        this.log(Level.INFO, "Registered Graou at '" + graouJson.get("cat").getAsString());
    }
}
