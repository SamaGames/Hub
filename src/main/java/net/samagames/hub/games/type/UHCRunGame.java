package net.samagames.hub.games.type;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.DisplayedStat;
import net.samagames.hub.games.shop.ShopCategory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class UHCRunGame extends AbstractGame
{
    public UHCRunGame()
    {

    }

    @Override
    public String getCodeName()
    {
        return "uhcrun";
    }

    @Override
    public String getName()
    {
        return "UHCRun";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.WATCH, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "Vous aimez les UHC mais vous n'avez",
                "pas le temps ? Ce jeu est fait pour vous !"
        };
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
    public List<DisplayedStat> getDisplayedStats()
    {
        List<DisplayedStat> stats = super.getDisplayedStats();
        stats.add(new DisplayedStat("kills", "Joueurs tués", new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal())));
        stats.add(new DisplayedStat("deaths", "Morts", Material.BONE));
        return stats;
    }

    @Override
    public Location getLobbySpawn()
    {
        return new Location(Hub.getInstance().getHubWorld(), 237.5D, 81.0D, 0.5D, 140.0F, 0.0F);
    }

    @Override
    public boolean isLocked()
    {
        return false;
    }
}
