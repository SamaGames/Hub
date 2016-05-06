package net.samagames.hub.games.types;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.shops.ShopCategory;
import net.samagames.hub.games.shops.ShopImprovableItem;
import net.samagames.tools.RulesBook;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class UppervoidGame extends AbstractGame
{
    public UppervoidGame(Hub hub)
    {
        super(hub);
    }

    @Override
    public String getCodeName()
    {
        return "uppervoid";
    }

    @Override
    public String getName()
    {
        return "Uppervoid";
    }

    @Override
    public String getCategory()
    {
        return "Seul contre tous";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.STICK, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "Affrontez les autres joueurs dans",
                "une arène. Faites-les tomber dans le",
                "vide à l'aide de vos impitoyables",
                "TNT's, mais gare à ne pas tomber",
                "à votre tour !"
        };
    }

    @Override
    public String[] getDevelopers()
    {
        return new String[] {
                "IamBlueSlime",
                "Silvanosky"
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
        return 20;
    }

    @Override
    public ShopCategory getShopConfiguration()
    {
        return null;
    }

    @Override
    public Location getLobbySpawn()
    {
        return new Location(this.hub.getWorld(), -7D, 83.0D, 67D, 0.0F, 0.0F);
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
