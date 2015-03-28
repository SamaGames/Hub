package net.samagames.hub.jump;

import net.samagames.hub.Hub;
import net.samagames.hub.common.JsonConfiguration;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.tools.LocationUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonArray;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonObject;
import org.bukkit.entity.Player;

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
        JsonArray jsonArray = jumpsConfig.getJsonArray("jumps");

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

    public void onPressurePlatePressed(Player player, Location plate)
    {
        Jump jump = this.getOfPlayer(player.getUniqueId());

        if (jump != null)
        {
            if (jump.getEnd().equals(plate))
            {
                jump.winPlayer(player);
                return;
            }
            else if (jump.getBegin().equals(plate))
            {
                return;
            }
            else
            {
                jump.removePlayer(player.getUniqueId());
            }
        }

        for (Jump jumpp : this.jumps)
        {
            if (jumpp.getBegin().equals(plate))
            {
                jumpp.addPlayer(player);
                return;
            }
        }
    }

    public void onFall(Player player)
    {
        Jump jump = this.getOfPlayer(player.getUniqueId());

        if (jump != null)
            jump.losePlayer(player);
    }

    public void logout(UUID player)
    {
        Jump jump = this.getOfPlayer(player);

        if (jump != null)
            jump.removePlayer(player);
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

    public boolean isInJump(UUID player)
    {
        for (Jump jump : this.jumps)
            if (jump.isJumping(player))
                return true;

        return false;
    }

    @Override
    public String getName() { return "JumpManager"; }
}


