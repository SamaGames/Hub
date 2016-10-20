package net.samagames.hub.games.types;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.leaderboards.HubLeaderboard;
import net.samagames.hub.games.shops.ShopCategory;
import net.samagames.tools.MojangShitUtils;
import net.samagames.tools.RulesBook;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BurnThatChickenGame extends AbstractGame
{
    public BurnThatChickenGame(Hub hub)
    {
        super(hub);
    }

    @Override
    public String getCodeName()
    {
        return "burnthatchicken";
    }

    @Override
    public String getName()
    {
        return "BurnThatChicken";
    }

    @Override
    public String getCategory()
    {
        return "Arcade";
    }

    @Override
    public ItemStack getIcon()
    {
        return MojangShitUtils.getMonsterEgg(EntityType.CHICKEN);
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
        return 32;
    }

    @Override
    public ShopCategory getShopConfiguration()
    {
        return null;
    }

    @Override
    public Location getLobbySpawn()
    {
        return new Location(this.hub.getWorld(), 29.5D, 104.0D, 32.5D, -67.7F, 0.0F);
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
        return false;
    }
}
