package net.samagames.hub.games;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.games.shop.ShopCategory;
import net.samagames.hub.games.sign.GameSign;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractGame
{
    private final HashMap<String, GameSign> signs;
    private boolean isMaintenance;

    public AbstractGame()
    {
        this.signs = new HashMap<>();

        Jedis jedis = SamaGamesAPI.get().getBungeeResource();
        if(jedis.exists("hub:maintenance:" + this.getCodeName()))
            this.setMaintenance(Boolean.valueOf(jedis.get("hub:maintenance:" + this.getCodeName())));
        else
            this.setMaintenance(false);
        jedis.close();
    }

    public abstract String getCodeName();
    public abstract String getName();
    public abstract ItemStack getIcon();
    public abstract String[] getDescription();
    public abstract int getSlotInMainMenu();
    public abstract ShopCategory getShopConfiguration();
    public abstract Location getLobbySpawn();
    public abstract boolean isLocked();

    public List<DisplayedStat> getDisplayedStats()
    {
        List<DisplayedStat> stats = new ArrayList<>();
        stats.add(new DisplayedStat("wins", "Victoires", Material.NETHER_STAR));
        stats.add(new DisplayedStat("played_games", "Parties jouées", Material.SIGN));
        return stats;
    }

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
