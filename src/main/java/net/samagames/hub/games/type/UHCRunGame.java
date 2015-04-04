package net.samagames.hub.games.type;

import net.samagames.hub.Hub;
import net.samagames.hub.games.IGame;
import net.samagames.hub.games.shop.ShopCategory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class UHCRunGame implements IGame
{
    @Override
    public String getCodeName()
    {
        return "uhcrun";
    }

    @Override
    public String getName()
    {
        return "UHCRun";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.WATCH, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "Vous aimes les UHC mais vous n'avez",
                "pas le temps ? Ce jeu est fait pour vous !"
        };
    }

    @Override
    public int getSlotInMainMenu()
    {
        return 23;
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
        return false;
    }
}
