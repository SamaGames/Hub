package net.samagames.hub.games.types;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.shops.ShopCategory;
import net.samagames.hub.games.shops.ShopDependsItem;
import net.samagames.hub.games.shops.ShopImprovableItem;
import net.samagames.tools.GlowEffect;
import net.samagames.tools.RulesBook;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class QuakeGame extends AbstractGame
{
    public QuakeGame(Hub hub)
    {
        super(hub);
    }

    @Override
    public String getCodeName()
    {
        return "quake";
    }

    @Override
    public String getName()
    {
        return "Quake";
    }

    @Override
    public String getCategory()
    {
        return "Match à mort";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.DIAMOND_HOE, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "Même si vous n'êtes pas un fermier,",
                "tirez sur vos adversaires avec",
                "votre houe !"
        };
    }

    @Override
    public String[] getDevelopers()
    {
        return new String[] {
                "Silvanosky"
        };
    }

    @Override
    public RulesBook[] getRulesBooks()
    {
        return null;
    }

    @Override
    public int getSlotInMainMenu()
    {
        return 24;
    }

    @Override
    public ShopCategory getShopConfiguration()
    {
        return null;
    }

    @Override
    public Location getLobbySpawn()
    {
        return new Location(this.hub.getWorld(), 31D, 102.0D, -31D, -90.0F, 0.0F);
    }

    @Override
    public boolean isGroup()
    {
        return false;
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
