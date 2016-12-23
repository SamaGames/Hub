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

public class ChunkWarsGame extends AbstractGame
{
    public ChunkWarsGame(Hub hub)
    {
        super(hub);
    }

    @Override
    public String getCodeName()
    {
        return "chunkwars";
    }

    @Override
    public String getName()
    {
        return "ChunkWars";
    }

    @Override
    public String getCategory()
    {
        return "PvP";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.ENDER_PORTAL_FRAME, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[0];
    }

    @Override
    public String[] getDevelopers()
    {
        return new String[] {
                "IamBlueSlime",
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
        return 22;
    }

    @Override
    public ShopCategory getShopConfiguration()
    {
        return null;
    }

    @Override
    public Location getLobbySpawn()
    {
        return this.hub.getGameManager().getGameByIdentifier("beta_vip").getLobbySpawn();
    }

    @Override
    public List<HubLeaderboard> getLeaderBoards()
    {
        return null;
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

    @Override
    public boolean isLocked()
    {
        return false;
    }

    @Override
    public boolean isNew()
    {
        return true;
    }
}
