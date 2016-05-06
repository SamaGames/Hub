package net.samagames.hub.games.types;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.shops.ShopCategory;
import net.samagames.tools.RulesBook;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class HeroBattleGame extends AbstractGame
{
    public HeroBattleGame(Hub hub)
    {
        super(hub);
    }

    @Override
    public String getCodeName()
    {
        return "herobattle";
    }

    @Override
    public String getName()
    {
        return "HeroBattle";
    }

    @Override
    public String getCategory()
    {
        return "PvP";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.MAGMA_CREAM);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "Tel que sur Smash, choisissez votre",
                "classe puis battez-vous dans des",
                "univers uniques !"
        };
    }

    @Override
    public String[] getDevelopers()
    {
        return new String[] {
                "AmauryPi",
                "6infinity8"
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
        return 21;
    }

    @Override
    public ShopCategory getShopConfiguration()
    {
        return null;
    }

    @Override
    public Location getLobbySpawn()
    {
        return new Location(this.hub.getWorld(), -53D, 114.0D, 35D, 0.0F, 0.0F);
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
