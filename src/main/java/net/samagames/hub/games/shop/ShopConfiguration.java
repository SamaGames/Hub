package net.samagames.hub.games.shop;

import java.util.ArrayList;

public class ShopConfiguration
{
    private final ArrayList<ShopIcon> shopItems;

    public ShopConfiguration()
    {
        this.shopItems = new ArrayList<>();
    }

    public void addShopItem(ShopIcon item)
    {
        if(!this.shopItems.contains(item))
            this.shopItems.add(item);
    }

    public ShopIcon getShopItemByName(String databaseName)
    {
        for(ShopIcon item : this.shopItems)
            if(item.getActionName().equals(databaseName))
                return item;

        return null;
    }

    public ArrayList<ShopIcon> getShopItems()
    {
        return this.shopItems;
    }
}
