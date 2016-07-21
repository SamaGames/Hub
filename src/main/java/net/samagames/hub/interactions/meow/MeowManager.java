package net.samagames.hub.interactions.meow;

import com.google.gson.JsonArray;
import net.minecraft.server.v1_9_R2.EntityOcelot;
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
import java.util.logging.Level;

public class MeowManager extends AbstractInteractionManager<Meow> implements Listener
{
    private static final List<Bonus> BONUS;

    private List<UUID> lock;

    public MeowManager(Hub hub)
    {
        super(hub, "meow");

        this.lock = new ArrayList<>();

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

    static Bonus getBonusById(int id)
    {
        for (Bonus bonus : BONUS)
            if (bonus.getId() == id)
                return bonus;

        return null;
    }

    static List<Bonus> getBonus()
    {
        return BONUS;
    }

    static
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        BONUS = new ArrayList<>();

        BONUS.add(new Bonus(0, 12, ChatColor.GOLD + "Bonus mensuel : " + ChatColor.GREEN + "VIP", new String[] {
                ChatColor.GRAY + "Afin de vous remercier pour",
                ChatColor.GRAY + "l'achat de votre grade,",
                ChatColor.GRAY + "acceptez ce modeste présent.",
                "",
                ChatColor.GRAY + "Contient :",
                ChatColor.WHITE + "- " + ChatColor.AQUA + "300 étoiles"
        }, 300, calendar.getActualMaximum(Calendar.DAY_OF_MONTH), Calendar.DAY_OF_YEAR, "network.vip"));

        BONUS.add(new Bonus(1, 14, ChatColor.GOLD + "Bonus mensuel : " + ChatColor.AQUA + "VIP" + ChatColor.LIGHT_PURPLE + "+", new String[] {
                ChatColor.GRAY + "Afin de vous remercier pour",
                ChatColor.GRAY + "l'achat de votre grade,",
                ChatColor.GRAY + "acceptez ce modeste présent.",
                "",
                ChatColor.GRAY + "Contient :",
                ChatColor.WHITE + "- " + ChatColor.AQUA + "700 étoiles"
        }, 700, calendar.getActualMaximum(Calendar.DAY_OF_MONTH), Calendar.DAY_OF_YEAR, "network.vipplus"));
    }
}
