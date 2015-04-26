package net.samagames.hub.games.type;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.DisplayedStat;
import net.samagames.hub.games.shop.ShopCategory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

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
                "Vous aimes les UHC mais vous n'avez",
                "pas le temps ? Ce jeu est fait pour vous !"
        };
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
    public ArrayList<DisplayedStat> getDisplayedStats()
    {
        ArrayList<DisplayedStat> stats = new ArrayList<>();

        stats.add(new DisplayedStat("victories", "Victoires", Material.NETHER_STAR));
        stats.add(new DisplayedStat("kills", "Joueurs tués", new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal())));

        return stats;
    }

    @Override
    public Location getLobbySpawn()
    {
        return Hub.getInstance().getPlayerManager().getLobbySpawn();
    }

    @Override
    public boolean isLocked()
    {
        return false;
    }
}
