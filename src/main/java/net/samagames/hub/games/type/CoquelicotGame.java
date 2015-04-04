package net.samagames.hub.games.type;

import net.samagames.hub.Hub;
import net.samagames.hub.games.IGame;
import net.samagames.hub.games.shop.ShopCategory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CoquelicotGame implements IGame
{
    @Override
    public String getCodeName()
    {
        return "coquelicot";
    }

    @Override
    public String getName()
    {
        return "Coquelicot";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.SKULL_ITEM, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "Il est prévu qu'une invasion de zombie",
                "survienne, serez-vous le premier à",
                "vous échappez de cet enfer ?"
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
        return Hub.getInstance().getPlayerManager().getLobbySpawn();
    }

    @Override
    public boolean isLocked()
    {
        return true;
    }
}
