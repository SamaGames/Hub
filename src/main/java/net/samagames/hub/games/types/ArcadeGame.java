package net.samagames.hub.games.types;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.shops.ShopCategory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ArcadeGame extends AbstractGame
{
    public ArcadeGame(Hub hub)
    {
        super(hub);
    }

    @Override
    public String getCodeName()
    {
        return "arcade";
    }

    @Override
    public String getName()
    {
        return "Arcade";
    }

    @Override
    public String getCategory()
    {
        return "Arcade";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.SLIME_BALL, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "Tout un tas de mini-jeux les plus",
                "improbables les uns que les autres !",
                "",
                "\u2B29 HangoverGames",
                "\u2B29 WitherParty",
                "\u2B29 BurnThatChicken",
                "\u2B29 PacMan",
                "\u2B29 Timberman",
                "\u2B29 Bomberman"
        };
    }

    @Override
    public String[] getDeveloppers()
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
        return new Location(this.hub.getWorld(), 107.5D, 86.0D, 138.5D, 0.0F, 0.0F);
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
