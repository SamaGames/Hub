package net.samagames.hub.parkour;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.samagames.hub.Hub;
import net.samagames.hub.common.JsonConfiguration;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.tools.LocationUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

public class ParkourManager extends AbstractManager
{
    private final ArrayList<Parkour> parkours;
    private final String tag;

    public ParkourManager(Hub hub)
    {
        super(hub);

        this.parkours = new ArrayList<>();
        this.tag = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Parkour" + ChatColor.DARK_AQUA + "] ";

        this.reloadConfig();
    }

    public void reloadConfig()
    {
        this.parkours.clear();
        this.loadConfig();
    }

    public void loadConfig()
    {
        JsonConfiguration parkoursConfig = new JsonConfiguration(new File(this.hub.getDataFolder(), "parkours.json"));

        if(parkoursConfig == null)
            return;

        JsonObject jsonRoot = parkoursConfig.load();

        if(jsonRoot == null)
            return;

        JsonArray jsonArray = jsonRoot.getAsJsonArray("parkours");

        for(int i = 0; i < jsonArray.size(); i++)
        {
            JsonObject jsonParkour = jsonArray.get(i).getAsJsonObject();

            String name = jsonParkour.get("name").getAsString();
            Location begin = LocationUtils.str2loc(jsonParkour.get("begin").getAsString());
            Location end = LocationUtils.str2loc(jsonParkour.get("end").getAsString());
            Location spawn = LocationUtils.str2loc(jsonParkour.get("spawn").getAsString());

            ArrayList<Material> whitelist = new ArrayList<>();

            JsonArray jsonMaterials = jsonParkour.get("whitelist").getAsJsonArray();

            for(int j = 0; j < jsonMaterials.size(); j++)
            {
                if(Material.matchMaterial(jsonMaterials.get(j).getAsString()) != null)
                {
                    whitelist.add(Material.matchMaterial(jsonMaterials.get(j).getAsString()));
                }
                else
                {
                    this.hub.log(this, Level.SEVERE, "Cannot parkour '" + jsonMaterials.get(j).getAsString() + "' to the parkour '" + name + "' whitelist!");
                }
            }

            String achievementName = null;

            if(jsonParkour.has("achievement"))
                achievementName = jsonParkour.get("achievement").getAsString();

            this.registerParkour(name, begin, end, spawn, whitelist, achievementName);
        }
    }

    public void registerParkour(String name, Location begin, Location end, Location spawn, ArrayList<Material> whitelist, String achievementName)
    {
        Parkour parkour = new Parkour(name, begin, end, spawn, whitelist, achievementName);

        if (!this.parkours.contains(parkour))
            this.parkours.add(parkour);

        this.hub.log(this, Level.INFO, "Registered parkour '" + name + "'");
    }

    public Parkour getOfPlayer(UUID player)
    {
        for (Parkour parkour : this.parkours)
            if (parkour.isParkouring(player))
                return parkour;

        return null;
    }

    public String getTag()
    {
        return this.tag;
    }

    public ArrayList<Parkour> getParkours()
    {
        return this.parkours;
    }

    @Override
    public String getName() { return "ParkourManager"; }
}


