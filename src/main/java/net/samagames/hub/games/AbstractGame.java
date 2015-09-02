package net.samagames.hub.games;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.games.shop.ShopCategory;
import net.samagames.hub.games.sign.GameSign;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractGame
{
    private final HashMap<String, GameSign> signs;
    private boolean isMaintenance;

    public AbstractGame()
    {
        this.signs = new HashMap<>();

        if(SamaGamesAPI.get().getResource().exists("hub:maintenance:" + this.getCodeName()))
            this.setMaintenance(Boolean.valueOf(SamaGamesAPI.get().getResource().get("hub:maintenance:" + this.getCodeName())));
        else
            this.setMaintenance(false);
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

    public void addSignForMap(String map, Sign sign, String template, ChatColor color)
    {
        this.signs.put(map, new GameSign(this, map, color, template, sign));
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

    public GameSign getGameSignByMap(String map)
    {
        if(this.signs.containsKey(map))
            return this.signs.get(map);
        else
            return null;
    }

    public HashMap<String, GameSign> getSigns()
    {
        return this.signs;
    }

    public boolean hasShop()
    {
        return this.getShopConfiguration() != null;
    }

    public boolean isMaintenance()
    {
        return this.isMaintenance;
    }

    public void setMaintenance(boolean flag)
    {
        this.isMaintenance = flag;
        this.signs.values().forEach(net.samagames.hub.games.sign.GameSign::update);
    }
}
