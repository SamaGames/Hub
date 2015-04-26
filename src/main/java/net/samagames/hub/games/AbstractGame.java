package net.samagames.hub.games;

import net.samagames.hub.games.shop.ShopCategory;
import net.samagames.hub.games.sign.GameSignZone;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractGame
{
    private final HashMap<String, GameSignZone> signZones;

    public AbstractGame()
    {
        this.signZones = new HashMap<>();
    }

    public abstract String getCodeName();
    public abstract String getName();
    public abstract ItemStack getIcon();
    public abstract String[] getDescription();
    public abstract int getSlotInMainMenu();
    public abstract ShopCategory getShopConfiguration();
    public abstract ArrayList<DisplayedStat> getDisplayedStats();
    public abstract Location getLobbySpawn();
    public abstract boolean isLocked();

    public void addSignZone(String map, GameSignZone zone)
    {
        this.signZones.put(map, zone);
    }

    public GameSignZone getGameSignZoneByMap(String map)
    {
        if(this.signZones.containsKey(map))
            return this.signZones.get(map);
        else
            return null;
    }

    public DisplayedStat getDisplayedStatByIdentifier(String identifier)
    {
        for(DisplayedStat stat : this.getDisplayedStats())
        {
            if(stat.getDatabaseName().equals(identifier))
                return stat;
        }

        return null;
    }

    public HashMap<String, GameSignZone> getSignZones()
    {
        return this.signZones;
    }

    public boolean hasShop()
    {
        return this.getShopConfiguration() != null;
    }
}
