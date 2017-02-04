package net.samagames.hub.interactions.bumper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteractionManager;

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
            JsonElement jsonBumber = rootJson.get(i);

            Bumper bumper = null;

            try
            {
                bumper = new Bumper(this.hub, jsonBumber.getAsString());
            }
            catch (Exception ignored) {}

            if (bumper != null)
            {
                this.interactions.add(bumper);
                this.log(Level.INFO, "Registered bumper at '" + jsonBumber.getAsString());
            }
        }
    }
}
