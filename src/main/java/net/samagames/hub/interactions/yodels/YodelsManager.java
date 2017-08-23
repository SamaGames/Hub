package net.samagames.hub.interactions.yodels;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteractionManager;
import net.samagames.tools.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
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

                ArmorStand startArmorStand = (ArmorStand) startPlatform.getWorld().getNearbyEntities(startPlatform, 4.0D, 4.0D, 4.0D).stream().filter(entity -> entity instanceof ArmorStand).filter(entity -> entity.getCustomName().equals(startRails)).findFirst().orElse(null);
                ArmorStand endArmorStand = (ArmorStand) endPlatform.getWorld().getNearbyEntities(endPlatform, 4.0D, 4.0D, 4.0D).stream().filter(entity -> entity instanceof ArmorStand).filter(entity -> entity.getCustomName().equals(endRails)).findFirst().orElse(null);

                if (startArmorStand == null || endArmorStand == null)
                {
                    this.log(Level.WARNING, "Can't find one of the two ends of yodel");
                    this.log(Level.WARNING, "More info : ");
                    this.log(Level.WARNING, "start-rails = " + startRails);
                    this.log(Level.WARNING, "end-rails = " + endRails);
                    this.log(Level.WARNING, "start-platform = " + jsonYodel.get("start-platform").getAsString());
                    this.log(Level.WARNING, "end-platform = " + jsonYodel.get("end-platform").getAsString());
                    this.log(Level.WARNING, "start-armorstand = " + (startArmorStand == null ? "NULL" : "NOTNULL" ));
                    this.log(Level.WARNING, "end-armorstand = " + (endArmorStand == null ? "NULL" : "NOTNULL"));
                    this.log(Level.WARNING, "--------------------------------------------");

                    continue;
                }

                Location startRailsLoc = startArmorStand.getLocation().clone().subtract(0.0D, 1.8D, 0.0D);
                Location endRailsLoc = endArmorStand.getLocation().clone().subtract(0.0D, 1.8D, 0.0D);

                this.interactions.add(new Yodel(this.hub, startPlatform, startRailsLoc, endRailsLoc, endPlatform));
                this.interactions.add(new Yodel(this.hub, endPlatform, endRailsLoc, startRailsLoc, startPlatform));

                this.log(Level.INFO, "Registered yodel at '" + jsonYodel.get("start-platform").getAsString());

            }
            catch (NullPointerException ignored) {}
        }
    }
}
