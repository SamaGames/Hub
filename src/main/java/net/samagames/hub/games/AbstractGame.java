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

    public AbstractGame()
    {
        this.signs = new HashMap<String, GameSign>()
        {
            @Override
            public void clear()
            {
                this.values().forEach(net.samagames.hub.games.sign.GameSign::onDelete);
                super.clear();
            }
        };
    }

    public abstract String getCodeName();
    public abstract String getName();
    public abstract ItemStack getIcon();
    public abstract String[] getDescription();
    public abstract String[] getDeveloppers();
    public abstract int getSlotInMainMenu();
    public abstract ShopCategory getShopConfiguration();
    public abstract Location getLobbySpawn();
    public abstract boolean isLocked();
    public abstract boolean isNew();

    public List<DisplayedStat> getDisplayedStats()
    {
        List<DisplayedStat> stats = new ArrayList<>();
        stats.add(new DisplayedStat("wins", "Victoires", Material.NETHER_STAR));
        stats.add(new DisplayedStat("played-games", "Parties jouées", Material.SIGN));
        return stats;
    }

    public void addSignForMap(String map, Sign sign, String template, ChatColor color)
    {
        GameSign gameSign = new GameSign(this, map, color, template, sign);

        Jedis jedis = SamaGamesAPI.get().getBungeeResource();

        if(jedis.exists("hub:maintenance:" + this.getCodeName() + ":" + template))
            gameSign.setMaintenance(Boolean.valueOf(jedis.get("hub:maintenance:" + this.getCodeName() + ":" + template)));

        jedis.close();

        this.signs.put(map, gameSign);
    }

    public void clearSigns()
    {
        this.signs.clear();
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

    public GameSign getGameSignByTemplate(String template)
    {
        for(GameSign sign : this.signs.values())
            if(sign.getTemplate().equals(template))
                return sign;

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
}
