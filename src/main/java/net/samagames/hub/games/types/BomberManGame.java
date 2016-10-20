package net.samagames.hub.games.types;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.leaderboards.HubLeaderboard;
import net.samagames.hub.games.shops.ShopCategory;
import net.samagames.tools.ItemUtils;
import net.samagames.tools.RulesBook;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BomberManGame extends AbstractGame
{
    public BomberManGame(Hub hub)
    {
        super(hub);
    }

    @Override
    public String getCodeName()
    {
        return "bomberman";
    }

    @Override
    public String getName()
    {
        return "BomberMan";
    }

    @Override
    public String getCategory()
    {
        return "Arcade";
    }

    @Override
    public ItemStack getIcon()
    {
        return ItemUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmM1NjNiOWI1ODI0MDlmNDFmMGUwNzgxYTk4M2FmZTNkOGZlMmZiZjM4M2M1M2E1ZDI3NDMxNTU1NjRkNjgifX19");
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
                "Azuxul"
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
        return new Location(this.hub.getWorld(), -53.5D, 113.0D, 36.5D, 38.0F, 0.0F);
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
