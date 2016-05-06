package net.samagames.hub.games.types;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.shops.ShopCategory;
import net.samagames.tools.RulesBook;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DimensionsGame extends AbstractGame
{
    public DimensionsGame(Hub hub)
    {
        super(hub);
    }

    @Override
    public String getCodeName()
    {
        return "dimensions";
    }

    @Override
    public String getName()
    {
        return "Dimensions";
    }

    @Override
    public String getCategory()
    {
        return "PvP";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.EYE_OF_ENDER, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "Téléportez-vous d'un monde parallèle",
                "à un autre et récupérez le maximum de",
                "coffres. Ensuite, le combat pourra",
                "débuter !"
        };
    }

    @Override
    public String[] getDevelopers()
    {
        return new String[] {
                "Silvanosky",
                "zyuiop"
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
        return new Location(this.hub.getWorld(), -13D, 87.0D, -51D, 180.0F, 0.0F);
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
