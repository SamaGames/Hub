package net.samagames.hub.games.types;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.shops.ShopCategory;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class BackEndGame extends AbstractGame
{
    private final String codeName;
    private final String publicName;
    private final Location spawn;

    public BackEndGame(Hub hub, String codeName, String publicName, Location spawn)
    {
        super(hub);

        this.codeName = codeName;
        this.publicName = publicName;
        this.spawn = spawn;
    }

    @Override
    public String getCodeName()
    {
        return this.codeName;
    }

    @Override
    public String getName()
    {
        return this.publicName;
    }

    @Override
    public String getCategory()
    {
        return null;
    }

    @Override
    public ItemStack getIcon()
    {
        return null;
    }

    @Override
    public String[] getDescription()
    {
        return null;
    }

    @Override
    public String[] getDeveloppers()
    {
        return null;
    }

    @Override
    public int getSlotInMainMenu()
    {
        return -1;
    }

    @Override
    public ShopCategory getShopConfiguration()
    {
        return null;
    }

    @Override
    public Location getLobbySpawn()
    {
        return this.spawn;
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
