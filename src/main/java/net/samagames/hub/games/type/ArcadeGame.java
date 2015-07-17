package net.samagames.hub.games.type;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.DisplayedStat;
import net.samagames.hub.games.shop.ShopCategory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ArcadeGame extends AbstractGame
{
    @Override
    public String getCodeName()
    {
        return "arcade";
    }

    @Override
    public String getName()
    {
        return "Arcade";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.SLIME_BALL, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "Tout un tas de mini-jeu les",
                "plus improbables les uns que",
                "les autres !"
        };
    }

    @Override
    public int getSlotInMainMenu()
    {
        return 22;
    }

    @Override
    public ShopCategory getShopConfiguration()
    {
        return null;
    }

    @Override
    public ArrayList<DisplayedStat> getDisplayedStats()
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
