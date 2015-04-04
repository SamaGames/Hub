package net.samagames.hub.games.shop;

import java.util.ArrayList;

public class ShopConfiguration
{
    private final int guiShopSlot;
    private final ArrayList<ShopItem> shopItems;

    public ShopConfiguration(int guiShopSlot)
    {
        this.guiShopSlot = guiShopSlot;
        this.shopItems = new ArrayList<>();
    }

    public void addShopItem(ShopItem item)
    {
        if(!this.shopItems.contains(item))
            this.shopItems.add(item);
    }

    public int getGuiShopSlot()
    {
        return this.guiShopSlot;
    }

    public ArrayList<ShopItem> getShopItems()
    {
        return this.shopItems;
    }
}
