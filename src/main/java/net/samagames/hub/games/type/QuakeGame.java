package net.samagames.hub.games.type;

import net.samagames.hub.Hub;
import net.samagames.hub.games.IGame;
import net.samagames.hub.games.shop.ShopCategory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class QuakeGame implements IGame
{
    @Override
    public String getCodeName()
    {
        return "quake";
    }

    @Override
    public String getName()
    {
        return "Quake Reborn";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.DIAMOND_HOE, 1);
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
        return 24;
    }

    @Override
    public ShopCategory getShopConfiguration()
    {
        return null;
    }

    @Override
    public Location getLobbySpawn()
    {
        return Hub.getInstance().getPlayerManager().getLobbySpawn();
    }

    @Override
    public boolean isLocked()
    {
        return true;
    }
}
