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

public class AgarMCGame extends AbstractGame
{
    public AgarMCGame(Hub hub)
    {
        super(hub);
    }

    @Override
    public String getCodeName()
    {
        return "agarmc";
    }

    @Override
    public String getName()
    {
        return "AgarMC";
    }

    @Override
    public String getCategory()
    {
        return "Seul contre tous";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.SLIME_BLOCK, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "Comme le célèbre jeu en ligne,",
                "venez avaler nos succulents Slimes.",
                "Il y en a pour tous les goûts :",
                "des petits comme des grands !"
        };
    }

    @Override
    public String[] getDevelopers()
    {
        return new String[] {
                "Rigner",
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
        return 23;
    }

    @Override
    public ShopCategory getShopConfiguration()
    {
        return null;
    }

    @Override
    public Location getLobbySpawn()
    {
        return new Location(this.hub.getWorld(), -69.5D, 104.0D, -44.5D, 124F, 0F);
    }

    @Override
    public List<HubLeaderboard> getLeaderBoards()
    {
        return null;
    }

    @Override
    public boolean hasResourcesPack()
    {
        return true;
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
