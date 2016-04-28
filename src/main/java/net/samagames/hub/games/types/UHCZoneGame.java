package net.samagames.hub.games.types;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.shops.ShopCategory;
import net.samagames.hub.games.signs.GameSign;
import net.samagames.tools.RulesBook;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class UHCZoneGame extends AbstractGame
{
    public UHCZoneGame(Hub hub)
    {
        super(hub);
    }

    @Override
    public String getCodeName()
    {
        return "uhczone";
    }

    @Override
    public String getName()
    {
        return "Zone UHC";
    }

    @Override
    public String getCategory()
    {
        return "PvP";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.GOLDEN_APPLE, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "La Zone où tous nos jeux Ultra Hard",
                "Core sont réunis !",
                "",
                "\u2B29 UHC",
                "\u2B29 UHCRun",
                "\u2B29 SwitchRun",
                "\u2B29 DoubleRunner",
                "\u2B29 UHCRandom"
        };
    }

    @Override
    public String[] getDevelopers()
    {
        return new String[] {
                "IamBlueSlime",
                "Rigner",
                "Thog"
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
        return 13;
    }

    @Override
    public ShopCategory getShopConfiguration()
    {
        return null;
    }

    @Override
    public Location getLobbySpawn()
    {
        return new Location(this.hub.getWorld(), 28.5D, 105.0D, 35.5D, -90.0F, 0.0F);
    }

    @Override
    public int getOnlinePlayers()
    {
        int players = 0;

        players += this.hub.getGameManager().getGameByIdentifier("uhc").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("uhcrun").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("switchrun").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("doublerunner").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("uhcrandom").getOnlinePlayers();

        return players;
    }

    @Override
    public boolean isGroup()
    {
        return true;
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
