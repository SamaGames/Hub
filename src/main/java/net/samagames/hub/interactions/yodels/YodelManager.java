package net.samagames.hub.interactions.yodels;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.server.v1_9_R1.EntityFireball;
import net.minecraft.server.v1_9_R1.EntitySmallFireball;
import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
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

public class YodelManager extends AbstractManager
{
    private final List<Yodel> yodels;

    public YodelManager(Hub hub)
    {
        super(hub);

        this.yodels = new ArrayList<>();
        this.hub.getEntityManager().registerEntity("Yodel", 13, EntitySmallFireball.class, EntityYodel.class);

        File yodelsConfigurationFile = new File(hub.getDataFolder(), "interactions" + File.separator + "yodels.json");

        if(!yodelsConfigurationFile.exists())
        {
            try
            {
                yodelsConfigurationFile.createNewFile();

                PrintWriter writer = new PrintWriter(yodelsConfigurationFile);
                writer.println("{ \"yodels\": [] }");
                writer.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        JsonConfiguration yodelsConfiguration = new JsonConfiguration(yodelsConfigurationFile);
        JsonObject jsonRoot = yodelsConfiguration.load();

        if(jsonRoot == null)
            return;

        JsonArray jsonYodels = jsonRoot.getAsJsonArray("yodels");

        for (int i = 0; i < jsonYodels.size(); i++)
        {
            JsonObject jsonYodel = jsonYodels.get(i).getAsJsonObject();

            String startRails = jsonYodel.get("start-rails").getAsString();
            String endRails = jsonYodel.get("end-rails").getAsString();
            Location startPlatform = LocationUtils.str2loc(jsonYodel.get("start-platform").getAsString());
            Location endPlatform = LocationUtils.str2loc(jsonYodel.get("end-platform").getAsString());

            this.yodels.add(new Yodel(this.hub, startRails, endRails, startPlatform, endPlatform));

            this.log(Level.INFO, "Registered yodel at '" + jsonYodel.get("start-platform").getAsString());
        }
    }

    @Override
    public void onDisable()
    {
        this.yodels.stream().forEach(Yodel::onDisable);
    }

    @Override
    public void onLogin(Player player) { /** Not Needed **/ }

    @Override
    public void onLogout(Player player)
    {
        this.yodels.stream().filter(yodel -> yodel.hasPlayer(player)).forEach(yodel -> yodel.stop(player));
    }
}
