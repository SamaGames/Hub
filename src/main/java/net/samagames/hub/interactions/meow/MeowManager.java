package net.samagames.hub.interactions.meow;

import com.google.gson.JsonArray;
import net.minecraft.server.v1_12_R1.EntityOcelot;
import net.samagames.api.games.pearls.Pearl;
import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteractionManager;
import net.samagames.tools.LocationUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.*;
import java.util.function.Consumer;
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
public class MeowManager extends AbstractInteractionManager<Meow> implements Listener
{
    private final List<Bonus> bonus;
    private List<UUID> lock;

    public MeowManager(Hub hub)
    {
        super(hub, "meow");

        this.lock = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        this.bonus = new ArrayList<>();

        this.bonus.add(new Bonus(0, 12, ChatColor.GOLD + "Bonus mensuel : " + ChatColor.GREEN + "VIP", new String[] {
                ChatColor.GRAY + "Afin de vous remercier pour",
                ChatColor.GRAY + "l'achat de votre grade,",
                ChatColor.GRAY + "acceptez ce modeste présent.",
                "",
                ChatColor.GRAY + "Contient :",
                ChatColor.WHITE + "- " + ChatColor.GREEN + "3 perles de niveau 4"
        }, calendar.getActualMaximum(Calendar.DAY_OF_MONTH), Calendar.DAY_OF_YEAR, "network.vip", player ->
        {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DAY_OF_MONTH, 7);

            for (int i = 0; i < 3; i++)
                this.hub.getInteractionManager().getWellManager().addPearlToPlayer(player, new Pearl(UUID.randomUUID(), 4, c.getTime().getTime()));
        }));

        this.bonus.add(new Bonus(1, 14, ChatColor.GOLD + "Bonus mensuel : " + ChatColor.AQUA + "VIP" + ChatColor.LIGHT_PURPLE + "+", new String[] {
                ChatColor.GRAY + "Afin de vous remercier pour",
                ChatColor.GRAY + "l'achat de votre grade,",
                ChatColor.GRAY + "acceptez ce modeste présent.",
                "",
                ChatColor.GRAY + "Contient :",
                ChatColor.WHITE + "- " + ChatColor.GREEN + "3 perles de niveau 5"
        }, calendar.getActualMaximum(Calendar.DAY_OF_MONTH), Calendar.DAY_OF_YEAR, "network.vipplus", player ->
        {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DAY_OF_MONTH, 7);

            for (int i = 0; i < 3; i++)
                this.hub.getInteractionManager().getWellManager().addPearlToPlayer(player, new Pearl(UUID.randomUUID(), 5, c.getTime().getTime()));
        }));

        this.hub.getServer().getPluginManager().registerEvents(this, this.hub);
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

            for (Meow meow : this.interactions)
            {
                if (meow.getMeowEntity().getBukkitEntity().getUniqueId().equals(event.getRightClicked().getUniqueId()))
                {
                    meow.play(event.getPlayer());
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

    public Bonus getBonusById(int id)
    {
        for (Bonus bonus : this.bonus)
            if (bonus.getId() == id)
                return bonus;

        return null;
    }

    public List<Bonus> getBonus()
    {
        return this.bonus;
    }
}
