package net.samagames.hub.parkours;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.parkours.types.DeveloperRoomParkour;
import net.samagames.hub.parkours.types.WhitelistBasedParkour;
import net.samagames.tools.JsonConfiguration;
import net.samagames.tools.LocationUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
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

    private void reloadConfig()
    {
        this.parkours.clear();
        this.loadConfig();
    }

    private void loadConfig()
    {
        File configuration = new File(this.hub.getDataFolder(), "parkours.json");

        if (!configuration.exists())
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

        if (jsonRoot == null)
            return;

        JsonArray jsonParkours = jsonRoot.getAsJsonArray("parkours");

        for (int i = 0; i < jsonParkours.size(); i++)
        {
            JsonObject jsonParkour = jsonParkours.get(i).getAsJsonObject();

            String name = jsonParkour.get("name").getAsString();
            String prefix = jsonParkour.get("prefix").getAsString();
            String winPrefix = jsonParkour.get("win-prefix").getAsString();
            int difficulty = jsonParkour.get("difficulty").getAsInt();
            Location begin = LocationUtils.str2loc(jsonParkour.get("begin").getAsString());
            Location end = LocationUtils.str2loc(jsonParkour.get("end").getAsString());
            Location fail = LocationUtils.str2loc(jsonParkour.get("fail").getAsString());
            int minimalHeight = jsonParkour.get("minimal-height").getAsInt();
            int lifesOnCheckpoint = jsonParkour.get("lifes-on-checkpoint").getAsInt();

            List<Location> checkpoints = new ArrayList<>();
            JsonArray jsonCheckpoints = jsonParkour.get("checkpoints").getAsJsonArray();

            for (int j = 0; j < jsonCheckpoints.size(); j++)
                checkpoints.add(LocationUtils.str2loc(jsonCheckpoints.get(j).getAsString()));

            List<Material> whitelist = new ArrayList<>();
            JsonArray jsonMaterials = jsonParkour.get("whitelist").getAsJsonArray();

            for (int j = 0; j < jsonMaterials.size(); j++)
            {
                String materialName = jsonMaterials.get(j).getAsString();

                if (Material.matchMaterial(materialName) != null)
                    whitelist.add(Material.matchMaterial(materialName));
                else
                    this.log(Level.SEVERE, "Cannot add the material '" + materialName + "' to the whitelist of the parkour '" + name + "'!");
            }

            int achievementId = jsonParkour.has("achievement") ? jsonParkour.get("achievement").getAsInt() : -1;

            this.registerWhitelistBasedParkour(name, prefix, winPrefix, begin, end, fail, minimalHeight, lifesOnCheckpoint, checkpoints, whitelist, difficulty, achievementId);
        }

        // -------------------

        if (jsonRoot.has("developpers-parkour"))
        {
            JsonObject jsonParkour = jsonRoot.getAsJsonObject("developpers-parkour");

            Location begin = LocationUtils.str2loc(jsonParkour.get("begin").getAsString());
            Location end = LocationUtils.str2loc(jsonParkour.get("end").getAsString());
            Location fail = LocationUtils.str2loc(jsonParkour.get("fail").getAsString());
            Location minusFloor = LocationUtils.str2loc(jsonParkour.get("minus-floor").getAsString());
            String resourcePack = jsonParkour.get("resource-pack").getAsString();

            JsonObject jsonPortals = jsonParkour.getAsJsonObject("portals");
            Pair<Location, Location> portals = Pair.of(LocationUtils.str2loc(jsonPortals.get("one").getAsString()), LocationUtils.str2loc(jsonPortals.get("two").getAsString()));

            this.registerParkour(new DeveloperRoomParkour(this.hub, begin, end, fail, portals, minusFloor, resourcePack));
        }
    }

    private void registerWhitelistBasedParkour(String name, String prefix, String winPrefix, Location begin, Location end, Location fail, int minimalHeight, int lifesOnCheckpoint, List<Location> checkpoints, List<Material> whitelist, int difficulty, int achievementId)
    {
        this.registerParkour(new WhitelistBasedParkour(this.hub, name, prefix, winPrefix, begin, end, fail, minimalHeight, lifesOnCheckpoint, checkpoints, whitelist, difficulty, achievementId));
    }

    private void registerParkour(Parkour parkour)
    {
        if (!this.parkours.contains(parkour))
            this.parkours.add(parkour);

        this.log(Level.INFO, "Registered parkour '" + parkour.getParkourName() + "'!");
    }
}