package net.samagames.hub.games;

import net.samagames.hub.games.shop.ShopConfiguration;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public interface IGame
{
    public String getCodeName();
    public String getName();
    public ItemStack getIcon();
    public String[] getDescription();
    public int getSlotInMainMenu();
    public ShopConfiguration getShopConfiguration();
    public Location getLobbySpawn();
    public boolean isLocked();

    public default boolean hasShop()
    {
        return this.getShopConfiguration() != null && this.getShopConfiguration().getGuiShopSlot() != -1;
    }
}
