package net.samagames.hub.games.type;

import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.shop.ShopCategory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SplatoonGame extends AbstractGame
{
    @Override
    public String getCodeName()
    {
        return "splatoon";
    }

    @Override
    public String getName()
    {
        return "Splatoon";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.INK_SACK, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "Prochainement..."
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
        return null;
    }

    @Override
    public boolean isLocked()
    {
        return true;
    }
}
