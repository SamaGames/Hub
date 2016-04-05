package net.samagames.hub.interactions.yodels;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.server.v1_9_R1.EntityFireball;
import net.minecraft.server.v1_9_R1.EntitySmallFireball;
import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.interactions.AbstractInteractionManager;
import net.samagames.tools.JsonConfiguration;
import net.samagames.tools.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class YodelManager extends AbstractInteractionManager<Yodel>
{
    public YodelManager(Hub hub)
    {
        super(hub, "yodels");

        this.hub.getEntityManager().registerEntity("Yodel", 13, EntitySmallFireball.class, EntityYodel.class);
    }

    @Override
    public void loadConfiguration(JsonArray rootJson)
    {
        for (int i = 0; i < rootJson.size(); i++)
        {
            JsonObject jsonYodel = rootJson.get(i).getAsJsonObject();

            String startRails = jsonYodel.get("start-rails").getAsString();
            String endRails = jsonYodel.get("end-rails").getAsString();
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
