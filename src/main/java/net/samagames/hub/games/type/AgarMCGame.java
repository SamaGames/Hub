package net.samagames.hub.games.type;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.shop.ShopCategory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AgarMCGame extends AbstractGame
{
    @Override
    public String getCodeName()
    {
        return "agarmc";
    }

    @Override
    public String getName()
    {
        return "AgarMC";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.SLIME_BLOCK, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "Comme le célèbre jeu en ligne, venez",
                "avalez nos succulents Slimes. Il y en a pour",
                "tous les goûts : des petits comme des grands !"
        };
    }

    @Override
    public String[] getDeveloppers()
    {
        return new String[] {
                "Rigner",
                "6infinity8"
        };
    }

    @Override
    public int getSlotInMainMenu()
    {
        return 32;
    }

    @Override
    public ShopCategory getShopConfiguration()
    {
        return null;
    }

    @Override
    public Location getLobbySpawn()
    {
        return new Location(Hub.getInstance().getHubWorld(), 150.5D, 87.0D, 46.5D);
    }

    @Override
    public boolean isLocked()
    {
        return false;
    }
}
