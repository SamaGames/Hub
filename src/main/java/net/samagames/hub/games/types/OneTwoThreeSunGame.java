package net.samagames.hub.games.types;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.leaderboards.HubLeaderboard;
import net.samagames.hub.games.shops.ShopCategory;
import net.samagames.tools.RulesBook;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class OneTwoThreeSunGame extends AbstractGame
{
    public OneTwoThreeSunGame(Hub hub)
    {
        super(hub);
    }

    @Override
    public String getCodeName()
    {
        return "onetwothreesun";
    }

    @Override
    public String getName()
    {
        return "123 Soleil";
    }

    @Override
    public String getCategory()
    {
        return "Arcade";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.DOUBLE_PLANT, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "TODO"
        };
    }

    @Override
    public String[] getDevelopers()
    {
        return new String[] {
                "Rigner"
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
        return new Location(this.hub.getWorld(), -12.5D, 86.0D, -51.5D, -118.0F, 0.0F);
    }

    @Override
    public List<HubLeaderboard> getLeaderBoards()
    {
        return null;
    }

    @Override
    public State getState()
    {
        return State.NEW;
    }

    @Override
    public boolean hasResourcesPack()
    {
        return false;
    }

    @Override
    public boolean isGroup()
    {
        return false;
    }
}
