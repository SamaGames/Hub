package net.samagames.hub.parkours.types;

import net.samagames.hub.Hub;
import net.samagames.hub.parkours.Parkour;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;

public class WhitelistBasedParkour extends Parkour
{
    private final List<Material> whitelist;

    public WhitelistBasedParkour(Hub hub, String prefix, String winPrefix, String parkourName, Location spawn, Location end, Location fail, int minimalHeight, List<Location> checkpoints, List<Material> whitelist, int difficulty, int achievementId)
    {
        super(hub, prefix, winPrefix, parkourName, spawn, end, fail, minimalHeight, checkpoints, difficulty, achievementId);

        this.whitelist = whitelist;

        this.whitelist.add(Material.AIR);
        this.whitelist.add(Material.TORCH);
        this.whitelist.add(Material.REDSTONE_TORCH_ON);
        this.whitelist.add(Material.REDSTONE_TORCH_OFF);
        this.whitelist.add(Material.IRON_PLATE);
        this.whitelist.add(Material.GOLD_PLATE);
        this.whitelist.add(Material.STONE_PLATE);
        this.whitelist.add(Material.WOOD_PLATE);
        this.whitelist.add(Material.SIGN);
        this.whitelist.add(Material.SIGN_POST);
        this.whitelist.add(Material.WALL_SIGN);
    }

    public boolean isWhitelisted(Material material)
    {
        return this.whitelist.contains(material);
    }
}
