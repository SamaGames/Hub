package net.samagames.hub.interactions.graou;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.server.v1_12_R1.EntityGuardian;
import net.minecraft.server.v1_12_R1.EntityOcelot;
import net.minecraft.server.v1_12_R1.EntitySquid;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.pearls.Pearl;
import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteractionManager;
import net.samagames.hub.interactions.graou.entity.EntityGraou;
import net.samagames.hub.interactions.graou.entity.EntityGraouLaser;
import net.samagames.hub.interactions.graou.entity.EntityGraouLaserTarget;
import net.samagames.tools.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.logging.Level;

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
public class GraouManager extends AbstractInteractionManager<Graou> implements Listener
{
    private final PearlLogic pearlLogic;
    private final List<UUID> lock;

    public GraouManager(Hub hub)
    {
        super(hub, "graou");

        this.pearlLogic = new PearlLogic(hub);
        this.lock = new ArrayList<>();

        this.hub.getServer().getPluginManager().registerEvents(this, this.hub);
        this.hub.getEntityManager().registerEntity("Graou", 98, EntityOcelot.class, EntityGraou.class);
        this.hub.getEntityManager().registerEntity("GraouLaser", 68, EntityGuardian.class, EntityGraouLaser.class);
        this.hub.getEntityManager().registerEntity("GraouLaserTarget", 94, EntitySquid.class, EntityGraouLaserTarget.class);
    }

    @Override
    public void onLogin(Player player)
    {
        super.onLogin(player);

        this.interactions.forEach(graou -> graou.onLogin(player));
    }

    @Override
    public void onLogout(Player player)
    {
        super.onLogout(player);

        this.interactions.forEach(graou -> graou.onLogout(player));

        if (this.lock.contains(player.getUniqueId()))
            this.lock.remove(player.getUniqueId());
    }

    public void update(Player player)
    {
        this.interactions.forEach(graou -> graou.update(player));
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

        for (int i = 0; i < rootJson.size(); i++)
        {
            JsonObject graouJson = rootJson.get(i).getAsJsonObject();

            Location catLocation = LocationUtils.str2loc(graouJson.get("cat").getAsString());
            Location doorLocation = LocationUtils.str2loc(graouJson.get("door").getAsString());
            Location treasureLocation = LocationUtils.str2loc(graouJson.get("treasure").getAsString());
            Location openingLocation = LocationUtils.str2loc(graouJson.get("opening").getAsString());

            Graou graou = new Graou(this.hub, catLocation, doorLocation, treasureLocation, openingLocation);

            this.interactions.add(graou);
            this.log(Level.INFO, "Registered Graou at '" + graouJson.get("cat").getAsString());
        }
    }

    public void deletePlayerPearl(UUID player, UUID pearl)
    {
        Jedis jedis = SamaGamesAPI.get().getBungeeResource();

        if (jedis == null)
            return;

        if (jedis.exists("pearls:" + player.toString() + ":" + pearl.toString()))
            jedis.del("pearls:" + player.toString() + ":" + pearl.toString());

        jedis.close();
    }

    public List<Pearl> getPlayerPearls(UUID player)
    {
        List<Pearl> pearls = new ArrayList<>();
        Jedis jedis = SamaGamesAPI.get().getBungeeResource();

        if (jedis == null)
            return pearls;

        for (String key : jedis.keys("pearls:" + player.toString() + ":*"))
            pearls.add(new Gson().fromJson(jedis.get(key), Pearl.class));

        jedis.close();

        Collections.sort(pearls, (o1, o2) -> o1.getStars() - o2.getStars());

        return pearls;
    }

    public PearlLogic getPearlLogic()
    {
        return this.pearlLogic;
    }
}
