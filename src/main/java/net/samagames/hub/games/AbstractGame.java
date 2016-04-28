package net.samagames.hub.games;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.games.shops.ShopCategory;
import net.samagames.hub.games.signs.GameSign;
import net.samagames.tools.RulesBook;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractGame
{
    protected final Hub hub;
    private final Map<String, List<GameSign>> signs;

    public AbstractGame(Hub hub)
    {
        this.hub = hub;

        this.signs = new HashMap<String, List<GameSign>>()
        {
            @Override
            public void clear()
            {
                this.values().forEach(list -> list.forEach(GameSign::onDelete));
                super.clear();
            }
        };
    }

    public abstract String getCodeName();
    public abstract String getName();
    public abstract String getCategory();
    public abstract ItemStack getIcon();
    public abstract String[] getDescription();
    public abstract String[] getDevelopers();
    public abstract RulesBook[] getRulesBooks();
    public abstract int getSlotInMainMenu();
    public abstract ShopCategory getShopConfiguration();
    public abstract Location getLobbySpawn();
    public abstract boolean isGroup();
    public abstract boolean isLocked();
    public abstract boolean isNew();

    public void addSignForMap(String map, Sign sign, String template, ChatColor color)
    {
        GameSign gameSign = new GameSign(this.hub, this, map, color, template, sign);

        Jedis jedis = SamaGamesAPI.get().getBungeeResource();

        if (jedis.exists("hub:maintenance:" + this.getCodeName() + ":" + template))
            gameSign.setMaintenance(Boolean.valueOf(jedis.get("hub:maintenance:" + this.getCodeName() + ":" + template)));

        jedis.close();

        if (this.signs.containsKey(map))
        {
            this.signs.get(map).add(gameSign);
        }
        else
        {
            List<GameSign> list = new ArrayList<>();
            list.add(gameSign);

            this.signs.put(map, list);
        }
    }

    public void clearSigns()
    {
        this.signs.clear();
    }

    public List<GameSign> getGameSignsByMap(String map)
    {
        if (this.signs.containsKey(map))
            return this.signs.get(map);
        else
            return null;
    }

    public List<GameSign> getGameSignsByTemplate(String template)
    {
        for (List<GameSign> list : this.signs.values())
            if (list.get(0).getTemplate().equals(template))
                return list;

        return null;
    }

    public int getOnlinePlayers()
    {
        int players = 0;

        for (List<GameSign> list : this.signs.values())
            for (GameSign sign : list)
                players += sign.getTotalPlayerOnServers();

        return players;
    }

    public Map<String, List<GameSign>> getSigns()
    {
        return this.signs;
    }

    public boolean hasShop()
    {
        return this.getShopConfiguration() != null;
    }
}
