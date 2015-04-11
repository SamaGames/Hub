package net.samagames.hub.games.type;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.shop.ShopCategory;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class HeroBattleGame extends AbstractGame
{
    @Override
    public String getCodeName()
    {
        return "herobattle";
    }

    @Override
    public String getName()
    {
        return "HeroBattle";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.INK_SACK, 1, DyeColor.RED.getDyeData());
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
        return 31;
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
