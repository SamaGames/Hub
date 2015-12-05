package net.samagames.hub.games.type;

import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.DisplayedStat;
import net.samagames.hub.games.shop.ShopCategory;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BackEndGame extends AbstractGame
{
    private final String codeName;
    private final String publicName;
    private final Location spawn;
    private final ArrayList<DisplayedStat> stats;

    public BackEndGame(String codeName, String publicName, Location spawn)
    {
        this.codeName = codeName;
        this.publicName = publicName;
        this.spawn = spawn;
        this.stats = null;
    }

    public BackEndGame(String codeName, String publicName, Location spawn, DisplayedStat... stats)
    {
        this.codeName = codeName;
        this.publicName = publicName;
        this.spawn = spawn;

        this.stats = new ArrayList<>();
        this.stats.addAll(Arrays.asList(stats));
        this.stats.addAll(super.getDisplayedStats());
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
        return this.stats;
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

    @Override
    public boolean isNew()
    {
        return false;
    }
}
