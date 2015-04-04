package net.samagames.hub.games.type;

import net.samagames.hub.Hub;
import net.samagames.hub.games.IGame;
import net.samagames.hub.games.shop.ShopConfiguration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class UHCGame implements IGame
{
    @Override
    public String getCodeName()
    {
        return "uhc";
    }

    @Override
    public String getName()
    {
        return "UHC";
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
                "Créez-vous votre stuff, tuez vos ennemis et",
                "soyez le dernier survivant ! Attention, il n'y",
                "a pas de régénération naturelle des coeurs."
        };
    }

    @Override
    public int getSlotInMainMenu()
    {
        return 12;
    }

    @Override
    public ShopConfiguration getShopConfiguration()
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
