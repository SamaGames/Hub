package net.samagames.hub.interactions.yodels;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteractionManager;
import net.samagames.tools.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import java.util.logging.Level;


public class YodelsManager extends AbstractInteractionManager<Yodel>
{

    public YodelsManager(Hub hub)
    {
        super(hub, "yodels");
    }

    @Override
    public void loadConfiguration(JsonArray rootJson)
    {
        for (int i = 0; i < rootJson.size(); i++)
        {
            try
            {
                JsonObject jsonYodel = rootJson.get(i).getAsJsonObject();

                String startRails = jsonYodel.get("start-rails").getAsString();
                String endRails = jsonYodel.get("end-rails").getAsString();

                Location startPlatform = LocationUtils.str2loc(jsonYodel.get("start-platform").getAsString());
                Location endPlatform = LocationUtils.str2loc(jsonYodel.get("end-platform").getAsString());

                ArmorStand startArmorStand = (ArmorStand) startPlatform.getWorld().getNearbyEntities(startPlatform, 8.0D, 8.0D, 8.0D).stream().filter(entity -> entity instanceof ArmorStand).filter(entity -> entity.getCustomName().equals(startRails)).findFirst().orElse(null);
                ArmorStand endArmorStand = (ArmorStand) endPlatform.getWorld().getNearbyEntities(endPlatform, 8.0D, 8.0D, 8.0D).stream().filter(entity -> entity instanceof ArmorStand).filter(entity -> entity.getCustomName().equals(endRails)).findFirst().orElse(null);

                if (startArmorStand == null || endArmorStand == null)
                {
                    this.log(Level.WARNING, "Can't find one of the two ends of yodel at " + jsonYodel.get("start-platform").getAsString());
                    continue;
                }

                Location startRailsLoc = startArmorStand.getLocation().clone().subtract(0.0D, 1.8D, 0.0D);
                Location endRailsLoc = endArmorStand.getLocation().clone().subtract(0.0D, 1.8D, 0.0D);

                Yodel yodel = new Yodel(this.hub, startRailsLoc, endRailsLoc, startPlatform, endPlatform);
                this.interactions.add(yodel);
                this.log(Level.INFO, "Registered yodel at '" + jsonYodel.get("start-platform").getAsString());

            }
            catch (NullPointerException ignored) {}
        }
    }
}
