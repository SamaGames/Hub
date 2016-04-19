package net.samagames.hub.interactions.bumper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteractionManager;
import net.samagames.tools.LocationUtils;
import org.bukkit.Location;

import java.util.logging.Level;

/**
 * Created by Rigner for project Hub.
 */
public class BumperManager extends AbstractInteractionManager<Bumper>
{
    public BumperManager(Hub hub)
    {
        super(hub, "bumpers");
    }

    @Override
    public void loadConfiguration(JsonArray rootJson)
    {
        for (int i = 0; i < rootJson.size(); i++)
        {
            JsonObject jsonBumber = rootJson.get(i).getAsJsonObject();

            Location location = LocationUtils.str2loc(jsonBumber.getAsString());

            Bumper bumper = null;

            try
            {
                bumper = new Bumper(hub, location);
            } catch (NullPointerException ignored) {}

            if (bumper != null)
            {
                this.interactions.add(bumper);
                this.log(Level.INFO, "Registered bumper at '" + jsonBumber.getAsString());
            }
        }
    }
}
