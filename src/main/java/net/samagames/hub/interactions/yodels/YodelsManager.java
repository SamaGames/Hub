package net.samagames.hub.interactions.yodels;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteractionManager;
import net.samagames.tools.LocationUtils;
import org.bukkit.Location;

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
            JsonObject jsonYodel = rootJson.get(i).getAsJsonObject();

            Location startRails = LocationUtils.str2loc(jsonYodel.get("start-rails").getAsString());
            Location endRails = LocationUtils.str2loc(jsonYodel.get("end-rails").getAsString());
            Location startPlatform = LocationUtils.str2loc(jsonYodel.get("start-platform").getAsString());
            Location endPlatform = LocationUtils.str2loc(jsonYodel.get("end-platform").getAsString());

            Yodel yodel = null;

            try
            {
                yodel = new Yodel(this.hub, startRails, endRails, startPlatform, endPlatform);
            }
            catch (NullPointerException ignored) {}

            if (yodel != null)
            {
                this.interactions.add(yodel);
                this.log(Level.INFO, "Registered yodel at '" + jsonYodel.get("start-platform").getAsString());
            }
        }
    }
}
