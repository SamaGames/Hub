package net.samagames.hub.games.types;

import net.samagames.api.stats.IPlayerStats;
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
                "Atteignez la ligne d'arrivée le",
                "premier, mais faites attention",
                "à l'oeil qui vous observe tel",
                "le jeu classique 1 2 3 Soleil"
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
    public String getWebsiteDescriptionURL()
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
        return new Location(this.hub.getWorld(), -71.5, 103.2D, -47.5D, 130.0F, 0.0F);
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
    public boolean isPlayerFirstGame(IPlayerStats playerStats)
    {
        return false;
    }

    @Override
    public boolean isGroup()
    {
        return false;
    }
}
