package net.samagames.hub.games.types;

import net.samagames.api.stats.IPlayerStats;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.leaderboards.HubLeaderboard;
import net.samagames.hub.games.shops.ShopCategory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BuildingJumpGame extends AbstractGame
{
    public BuildingJumpGame(Hub hub)
    {
        super(hub);
    }

    @Override
    public String getCodeName()
    {
        return "buildingjump";
    }

    @Override
    public String getName()
    {
        return "Building Jump";
    }

    @Override
    public String getCategory()
    {
        return "Arcade";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.PRISMARINE_SHARD, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {

        };
    }

    @Override
    public String[] getDevelopers()
    {
        return new String[] {
                "AmauryPi",
                "Reelwens",
                "Aurelien_Sama"
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
        return 31;
    }

    @Override
    public ShopCategory getShopConfiguration()
    {
        return null;
    }

    @Override
    public Location getLobbySpawn()
    {
        return new Location(this.hub.getWorld(), -42.5, 105.0D, -31.5D, 130.0F, 0.0F);
    }

    @Override
    public Location getWebsiteDescriptionSkull()
    {
        return new Location(this.hub.getWorld(), -44.0, 106.0D, -43.0D, 0.0F, 0.0F);
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
