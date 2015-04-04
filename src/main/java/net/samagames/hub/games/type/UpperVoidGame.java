package net.samagames.hub.games.type;

import net.samagames.hub.Hub;
import net.samagames.hub.games.IGame;
import net.samagames.hub.games.shop.ShopConfiguration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class UpperVoidGame implements IGame
{
    @Override
    public String getCodeName()
    {
        return "uppervoid";
    }

    @Override
    public String getName()
    {
        return "UpperVoid";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.STICK, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "Affrontez les autres joueurs dans une arène.",
                "Faites-les tomber dans le vide à l'aide de vos",
                "impitoyables TNT's, mais gare à ne pas tomber à",
                "votre tour !"
        };
    }

    @Override
    public int getSlotInMainMenu()
    {
        return 20;
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
