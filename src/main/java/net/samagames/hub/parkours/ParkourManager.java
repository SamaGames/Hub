package net.samagames.hub.parkours;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.tools.JsonConfiguration;
import net.samagames.tools.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class ParkourManager extends AbstractManager
{
    private final List<Parkour> parkours;

    public ParkourManager(Hub hub)
    {
        super(hub);

        this.parkours = new ArrayList<>();
        this.reloadConfig();
    }

    @Override
    public void onDisable()
    {
        this.parkours.forEach(Parkour::onDisable);
    }

    @Override
    public void onLogin(Player player) { /** Not needed **/ }

    @Override
    public void onLogout(Player player) { /** Not needed **/ }

    public void reloadConfig()
    {
        this.parkours.clear();
        this.loadConfig();
    }

    public void loadConfig()
    {
        File configuration = new File(this.hub.getDataFolder(), "parkours.json");

        if(!configuration.exists())
        {
            try
            {
                configuration.createNewFile();

                PrintWriter writer = new PrintWriter(configuration);
                writer.println("{ \"parkours\": [] }");
                writer.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        JsonConfiguration parkoursConfig = new JsonConfiguration(configuration);
        JsonObject jsonRoot = parkoursConfig.load();

        if(jsonRoot == null)
            return;

        JsonArray jsonParkours = jsonRoot.getAsJsonArray("parkours");

        for(int i = 0; i < jsonParkours.size(); i++)
        {
            JsonObject jsonParkour = jsonParkours.get(i).getAsJsonObject();

            String name = jsonParkour.get("name").getAsString();
            int difficulty = jsonParkour.get("difficulty").getAsInt();
            Location begin = LocationUtils.str2loc(jsonParkour.get("begin").getAsString());
            Location end = LocationUtils.str2loc(jsonParkour.get("end").getAsString());
            Location fail = LocationUtils.str2loc(jsonParkour.get("fail").getAsString());
            int minimalHeight = jsonParkour.get("minimal-height").getAsInt();

            List<Location> checkpoints = new ArrayList<>();
            JsonArray jsonCheckpoints = jsonParkour.get("checkpoints").getAsJsonArray();

            for(int j = 0; j < jsonCheckpoints.size(); j++)
                checkpoints.add(LocationUtils.str2loc(jsonCheckpoints.get(j).getAsString()));

            List<Material> whitelist = new ArrayList<>();
            JsonArray jsonMaterials = jsonParkour.get("whitelist").getAsJsonArray();

            for(int j = 0; j < jsonMaterials.size(); j++)
            {
                String materialName = jsonMaterials.get(j).getAsString();

                if(Material.matchMaterial(materialName) != null)
                    whitelist.add(Material.matchMaterial(materialName));
                else
                    this.log(Level.SEVERE, "Cannot add the material '" + materialName + "' to the whitelist of the parkour '" + name + "'!");
            }

            String achievementName = jsonParkour.has("achievement") ? jsonParkour.get("achievement").getAsString() : null;

            this.registerParkour(name, begin, end, fail, minimalHeight, checkpoints, whitelist, difficulty, achievementName);
        }
    }

    public void registerParkour(String name, Location begin, Location end, Location fail, int minimalHeight, List<Location> checkpoints, List<Material> whitelist, int difficulty, String achievementName)
    {
        Parkour parkour = new Parkour(this.hub, name, begin, end, fail, minimalHeight, checkpoints, whitelist, difficulty, achievementName);

        if (!this.parkours.contains(parkour))
            this.parkours.add(parkour);

        this.log(Level.INFO, "Registered parkour '" + name + "'!");
    }

    public Parkour getPlayerParkour(UUID player)
    {
        for (Parkour parkour : this.parkours)
            if (parkour.isParkouring(player))
                return parkour;

        return null;
    }

    public List<Parkour> getParkours()
    {
        return this.parkours;
    }
}