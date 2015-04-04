package net.samagames.hub.games.type;

import net.samagames.hub.Hub;
import net.samagames.hub.games.IGame;
import net.samagames.hub.games.shop.ShopConfiguration;
import net.samagames.hub.games.shop.ShopItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DimensionsGame implements IGame
{
    @Override
    public String getCodeName()
    {
        return "dimensions";
    }

    @Override
    public String getName()
    {
        return "Dimensions";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.EYE_OF_ENDER, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "Téléportez-vous d'un monde parallèle",
                "à un autre et récupérez le maximum de coffres.",
                "Ensuite, le combat pourra débuter !"
        };
    }

    @Override
    public int getSlotInMainMenu()
    {
        return 13;
    }

    @Override
    public ShopConfiguration getShopConfiguration()
    {
        ShopConfiguration shopConfiguration = new ShopConfiguration(13);

        ShopItem healOfKillItem = new ShopItem("dimensions.healofkill", "Soin au meurtre", new ItemStack(Material.SPECKLED_MELON, 1));
        healOfKillItem.addLevel(400, new String[]{});

        shopConfiguration.addShopItem(healOfKillItem);

        return shopConfiguration;
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
