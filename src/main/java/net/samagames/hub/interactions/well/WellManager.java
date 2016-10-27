package net.samagames.hub.interactions.well;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteractionManager;
import net.samagames.tools.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;
import java.util.logging.Level;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 27/10/2016
 */
public class WellManager extends AbstractInteractionManager<Well> implements Listener
{
    public WellManager(Hub hub)
    {
        super(hub, "well");
    }

    @Override
    public void onLogin(Player player)
    {
        super.onLogin(player);

        this.interactions.forEach(well -> well.onLogin(player));
    }

    @Override
    public void onLogout(Player player)
    {
        super.onLogout(player);

        this.interactions.forEach(well -> well.onLogout(player));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CAULDRON)
        {
            Optional<Well> optional = this.interactions.stream().filter(well -> normalize(well.getCauldronLocation()).equals(normalize(event.getClickedBlock().getLocation()))).findAny();

            if (optional.isPresent())
                optional.get().play(event.getPlayer());
        }
    }

    @Override
    public void loadConfiguration(JsonArray rootJson)
    {
        if (rootJson.size() == 0)
            return;

        for (int i = 0; i < rootJson.size(); i++)
        {
            JsonObject wellJson = rootJson.get(i).getAsJsonObject();

            Location cauldronLocation = LocationUtils.str2loc(wellJson.get("cauldron").getAsString());
            Location standsLocation = LocationUtils.str2loc(wellJson.get("stands").getAsString());

            Well well = new Well(this.hub, cauldronLocation, standsLocation);

            this.interactions.add(well);
            this.log(Level.INFO, "Registered Well at '" + wellJson.get("cauldron").getAsString());
        }
    }

    private static Location normalize(Location location)
    {
        return new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
