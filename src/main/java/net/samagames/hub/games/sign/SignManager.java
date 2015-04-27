package net.samagames.hub.games.sign;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.samagames.hub.Hub;
import net.samagames.hub.common.JsonConfiguration;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.games.AbstractGame;
import net.samagames.tools.LocationUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class SignManager extends AbstractManager
{
    private final JsonConfiguration jsonConfig;

    public SignManager(Hub hub)
    {
        super(hub);

        File config = new File(this.hub.getDataFolder(), "signs.json");

        if(!config.exists())
        {
            try
            {
                config.createNewFile();

                FileWriter fw = new FileWriter(config.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("{\"zones\":[]}");
                bw.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        this.jsonConfig = new JsonConfiguration(config);
        this.reloadList();
    }

    public void reloadList()
    {
        this.hub.log(this, Level.INFO, "Reloading game sign list...");

        JsonArray signZonesArray = this.jsonConfig.load().getAsJsonArray("zones");

        for(int i = 0; i < signZonesArray.size(); i++)
        {
            JsonObject signZoneObject = signZonesArray.get(i).getAsJsonObject();

            String game = signZoneObject.get("game").getAsString();

            JsonArray maps = signZoneObject.get("maps").getAsJsonArray();

            for(int j = 0; j < maps.size(); j++)
            {
                JsonObject mapObject = maps.get(j).getAsJsonObject();
                String map = mapObject.get("map").getAsString();
                Location sign = LocationUtils.str2loc(mapObject.get("sign").getAsString());

                AbstractGame gameObject = this.hub.getGameManager().getGameByIdentifier(game);
                Block block = Hub.getInstance().getHubWorld().getBlockAt(sign);

                if(!(block instanceof Sign))
                {
                    this.hub.log(this, Level.SEVERE, "Sign block for game '" + game + "' and map '" + map + "' is not a sign in the world!");
                    continue;
                }

                gameObject.addSignForMap(map, (Sign) Hub.getInstance().getHubWorld().getBlockAt(sign));

                this.hub.log(this, Level.INFO, "Registered sign zone for the game '" + game + "' and the map '" + map + "'!");
            }
        }

        this.hub.log(this, Level.INFO, "Reloaded game sign list.");
    }

    public void addZone(Player player, String game, String map, ArrayList<Sign> signs)
    {
        JsonObject root = this.jsonConfig.load();
        JsonArray signZonesArray = root.getAsJsonArray("zones");

        player.sendMessage(ChatColor.GREEN + "Starting job...");

        for(int i = 0; i < signZonesArray.size(); i++)
        {
            JsonObject signZoneObject = signZonesArray.get(i).getAsJsonObject();

            if(signZoneObject.get("game").getAsString().equals(game))
            {
                player.sendMessage(ChatColor.GREEN + "Game existing.");

                JsonArray maps = signZoneObject.get("maps").getAsJsonArray();

                for(int j = 0; j < maps.size(); j++)
                {
                    JsonObject mapObject = maps.get(j).getAsJsonObject();

                    if (mapObject.get("map").getAsString().equals(map))
                    {
                        player.sendMessage(ChatColor.GREEN + "Map existing.");

                        JsonArray signsArray = mapObject.get("signs").getAsJsonArray();

                        for (Sign sign : signs)
                        {
                            signsArray.add(new JsonPrimitive(LocationUtils.loc2str(sign.getLocation())));
                            player.sendMessage(ChatColor.GREEN + "Added sign (" + LocationUtils.loc2str(sign.getLocation()) + ")");
                        }

                        mapObject.add("signs", signsArray);

                        player.sendMessage(ChatColor.GREEN + "Job finished.");
                        this.jsonConfig.save(root);
                        this.reloadList();
                        return;
                    }
                }

                JsonObject mapObject = new JsonObject();
                mapObject.addProperty("map", map);

                JsonArray signsArray = new JsonArray();

                for (Sign sign : signs)
                {
                    signsArray.add(new JsonPrimitive(LocationUtils.loc2str(sign.getLocation())));
                    player.sendMessage(ChatColor.GREEN + "Added sign (" + LocationUtils.loc2str(sign.getLocation()) + ")");
                }

                mapObject.add("signs", signsArray);
                maps.add(mapObject);

                player.sendMessage(ChatColor.GREEN + "Job finished.");
                this.jsonConfig.save(root);
                this.reloadList();
                return;
            }
        }

        player.sendMessage(ChatColor.RED + "Game don't exist!");

        JsonObject signZoneObject = new JsonObject();
        signZoneObject.addProperty("game", game);

        JsonArray maps = new JsonArray();

        JsonObject mapObject = new JsonObject();
        mapObject.addProperty("map", map);

        JsonArray signsArray = new JsonArray();

        for (Sign sign : signs)
        {
            signsArray.add(new JsonPrimitive(LocationUtils.loc2str(sign.getLocation())));
            player.sendMessage(ChatColor.GREEN + "Added sign (" + LocationUtils.loc2str(sign.getLocation()) + ")");
        }

        mapObject.add("signs", signsArray);
        maps.add(mapObject);

        signZoneObject.add("maps", maps);
        signZonesArray.add(signZoneObject);

        player.sendMessage(ChatColor.GREEN + "Job finished.");
        this.jsonConfig.save(root);
        this.reloadList();
    }

    @Override
    public String getName() { return "SignManager"; }
}
