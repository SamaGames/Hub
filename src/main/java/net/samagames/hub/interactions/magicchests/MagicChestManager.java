package net.samagames.hub.interactions.magicchests;

import com.google.gson.JsonArray;
import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteractionManager;
import net.samagames.tools.LocationUtils;
import org.bukkit.Location;

import java.util.logging.Level;

public class MagicChestManager extends AbstractInteractionManager<MagicChest>
{
    public MagicChestManager(Hub hub)
    {
        super(hub, "magic-chests");
    }

    @Override
    public void loadConfiguration(JsonArray rootJson)
    {
        for (int i = 0; i < rootJson.size(); i++)
        {
            Location location = LocationUtils.str2loc(rootJson.get(i).getAsString());
            MagicChest magicChest = new MagicChest(this.hub, location);

            this.interactions.add(magicChest);
            this.log(Level.INFO, "Registered magic chest at '" + rootJson.get(i).getAsString());
        }
    }
}
