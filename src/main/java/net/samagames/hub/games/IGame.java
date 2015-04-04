package net.samagames.hub.games;

import net.samagames.hub.games.shop.ShopCategory;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public interface IGame
{
    String getCodeName();
    String getName();
    ItemStack getIcon();
    String[] getDescription();
    int getSlotInMainMenu();
    ShopCategory getShopConfiguration();
    Location getLobbySpawn();
    boolean isLocked();

    default boolean hasShop()
    {
        return this.getShopConfiguration() != null;
    }
}
