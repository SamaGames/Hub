package net.samagames.hub.games.types;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.shops.ShopCategory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class UHCZoneGame extends AbstractGame
{
    public UHCZoneGame(Hub hub)
    {
        super(hub);
    }

    @Override
    public String getCodeName()
    {
        return "uhczone";
    }

    @Override
    public String getName()
    {
        return "Zone UHC";
    }

    @Override
    public String getCategory()
    {
        return "PvP";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.GOLDEN_APPLE, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "La Zone où tous nos jeux Ultra Hard",
                "Core sont réunis !",
                "",
                "\u2B29 UHC",
                "\u2B29 UHCRun",
                "\u2B29 SwitchRun",
                "\u2B29 DoubleRunner",
        };
    }

    @Override
    public String[] getDeveloppers()
    {
        return new String[] {
                "IamBlueSlime"
        };
    }

    @Override
    public int getSlotInMainMenu()
    {
        return 13;
    }

    @Override
    public ShopCategory getShopConfiguration()
    {
        return null;
    }

    @Override
    public Location getLobbySpawn()
    {
        return new Location(this.hub.getWorld(), -1.5D, 81.0D, -152.5D, 180.0F, 0.0F);
    }

    @Override
    public boolean isLocked()
    {
        return false;
    }

    @Override
    public boolean isNew()
    {
        return false;
    }
}
