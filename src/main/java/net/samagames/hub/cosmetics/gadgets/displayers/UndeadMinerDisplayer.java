package net.samagames.hub.cosmetics.gadgets.displayers;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class UndeadMinerDisplayer extends AbstractDisplayer
{
    private int loopId;

    public UndeadMinerDisplayer(Player player)
    {
        super(player);
    }

    @Override
    public void display()
    {
        final ArrayList<Material> ores = new ArrayList<>();
        ores.add(Material.COAL_ORE);
        ores.add(Material.IRON_ORE);
        ores.add(Material.GOLD_ORE);
        ores.add(Material.REDSTONE_ORE);
        ores.add(Material.DIAMOND_ORE);
        ores.add(Material.EMERALD_ORE);
    }

    @Override
    public boolean canUse()
    {
        return false;
    }
}
