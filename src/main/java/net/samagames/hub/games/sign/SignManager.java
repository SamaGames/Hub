package net.samagames.hub.games.sign;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

import java.io.*;
import java.nio.charset.Charset;
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

                OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(config), Charset.forName("UTF-8"));
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

        this.hub.getGameManager().getGames().values().forEach(net.samagames.hub.games.AbstractGame::clearSigns);

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
                String template = mapObject.get("template").getAsString();
                ChatColor color = ChatColor.valueOf(mapObject.get("color").getAsString());
                Location sign = LocationUtils.str2loc(mapObject.get("sign").getAsString());

                AbstractGame gameObject = this.hub.getGameManager().getGameByIdentifier(game);
                Block block = Hub.getInstance().getHubWorld().getBlockAt(sign);

                if(!(block.getState() instanceof Sign))
                {
                    this.hub.log(this, Level.SEVERE, "Sign block for game '" + game + "' and map '" + map + "' is not a sign in the world!");
                    continue;
                }

                gameObject.addSignForMap(map.replace("_", " "), (Sign) Hub.getInstance().getHubWorld().getBlockAt(sign).getState(), template, color);

                this.hub.log(this, Level.INFO, "Registered sign zone for the game '" + game + "' and the map '" + map + "'!");
            }
        }

        this.hub.log(this, Level.INFO, "Reloaded game sign list.");
    }

    public void setSignForMap(Player player, String game, String map, ChatColor color, String template, Sign sign)
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

                JsonObject mapObject = new JsonObject();
                mapObject.addProperty("map", map);
                mapObject.addProperty("template", template);
                mapObject.addProperty("color", color.name());
                mapObject.addProperty("sign", LocationUtils.loc2str(sign.getLocation()));
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
        mapObject.addProperty("template", template);
        mapObject.addProperty("color", color.name());
        mapObject.addProperty("sign", LocationUtils.loc2str(sign.getLocation()));
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
