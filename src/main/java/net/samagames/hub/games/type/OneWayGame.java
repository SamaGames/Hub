package net.samagames.hub.games.type;

import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.DisplayedStat;
import net.samagames.hub.games.shop.ShopCategory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class OneWayGame extends AbstractGame
{
    private final String codeName;
    private final ItemStack icon;
    private final Location spawn;

    public OneWayGame(String codeName, ItemStack icon, Location spawn)
    {
        this.codeName = codeName;
        this.icon = icon;
        this.spawn = spawn;
    }

    public OneWayGame(String codeName, Material icon, Location spawn)
    {
        this(codeName, new ItemStack(icon, 1), spawn);
    }

    @Override
    public String getCodeName()
    {
        return this.codeName;
    }

    @Override
    public String getName()
    {
        return null;
    }

    @Override
    public ItemStack getIcon()
    {
        return this.icon;
    }

    @Override
    public String[] getDescription()
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
