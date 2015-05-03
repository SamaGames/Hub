package net.samagames.hub.jump;

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

public class JumpManager extends AbstractManager
{
    private final ArrayList<Jump> jumps;
    private final String tag;

    public JumpManager(Hub hub)
    {
        super(hub);

        this.jumps = new ArrayList<>();
        this.tag = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Jump" + ChatColor.DARK_AQUA + "] ";

        this.reloadConfig();
    }

    public void reloadConfig()
    {
        this.jumps.clear();
        this.loadConfig();
    }

    public void loadConfig()
    {
        JsonConfiguration jumpsConfig = new JsonConfiguration(new File(this.hub.getDataFolder(), "jumps.json"));
        JsonObject jsonRoot = jumpsConfig.load();

        if(jsonRoot == null)
            return;

        JsonArray jsonArray = jsonRoot.getAsJsonArray("jumps");

        for(int i = 0; i < jsonArray.size(); i++)
        {
            JsonObject jsonJump = jsonArray.get(i).getAsJsonObject();

            String name = jsonJump.get("name").getAsString();
            Location begin = LocationUtils.str2loc(jsonJump.get("begin").getAsString());
            Location end = LocationUtils.str2loc(jsonJump.get("end").getAsString());
            Location spawn = LocationUtils.str2loc(jsonJump.get("spawn").getAsString());
            ArrayList<Material> whitelist = new ArrayList<>();

            JsonArray jsonMaterials = jsonJump.get("whitelist").getAsJsonArray();

            for(int j = 0; j < jsonMaterials.size(); j++)
            {
                if(Material.matchMaterial(jsonMaterials.get(j).getAsString()) != null)
                {
                    whitelist.add(Material.matchMaterial(jsonMaterials.get(j).getAsString()));
                }
                else
                {
                    this.hub.log(this, Level.SEVERE, "Cannot add '" + jsonMaterials.get(j).getAsString() + "' to the jump '" + name + "' whitelist!");
                }
            }

            String achievementName = null;

            if(jsonJump.has("achievement"))
                achievementName = jsonJump.get("achievement").getAsString();

            this.registerJump(name, begin, end, spawn, whitelist, achievementName);
        }
    }

    public void registerJump(String name, Location begin, Location end, Location spawn, ArrayList<Material> whitelist, String achievementName)
    {
        Jump jump = new Jump(name, begin, end, spawn, whitelist, achievementName);

        if (!this.jumps.contains(jump))
            this.jumps.add(jump);

        this.hub.log(this, Level.INFO, "Registered jump '" + name + "'");
    }

    public Jump getOfPlayer(UUID player)
    {
        for (Jump jump : this.jumps)
            if (jump.isJumping(player))
                return jump;

        return null;
    }

    public String getTag()
    {
        return this.tag;
    }

    public ArrayList<Jump> getJumps()
    {
        return this.jumps;
    }

    @Override
    public String getName() { return "JumpManager"; }
}


