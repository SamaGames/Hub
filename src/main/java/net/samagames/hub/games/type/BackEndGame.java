package net.samagames.hub.games.type;

import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.DisplayedStat;
import net.samagames.hub.games.shop.ShopCategory;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class BackEndGame extends AbstractGame
{
    private final String codeName;
    private final String publicName;
    private final Location spawn;

    public BackEndGame(String codeName, String publicName, Location spawn)
    {
        this.codeName = codeName;
        this.publicName = publicName;
        this.spawn = spawn;
    }

    @Override
    public String getCodeName()
    {
        return this.codeName;
    }

    @Override
    public String getName() {
        return this.publicName;
    }

    @Override
    public ItemStack getIcon()
    {
        return null;
    }

    @Override
    public String[] getDescription()
    {
        return null;
    }

    @Override
    public String[] getDeveloppers()
    {
        return null;
    }

    @Override
    public int getSlotInMainMenu()
    {
        return -1;
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
        return this.spawn;
    }

    @Override
    public boolean isLocked()
    {
        return false;
    }
}
