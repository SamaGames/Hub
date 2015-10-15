package net.samagames.hub.games.type;

import net.md_5.bungee.api.ChatColor;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.DisplayedStat;
import net.samagames.hub.games.shop.ShopCategory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AgarMCGame extends AbstractGame
{
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
    public String getGuiName()
    {
        return "" + ChatColor.AQUA + ChatColor.BOLD + "[NEW] " + ChatColor.RED + ChatColor.BOLD + "[ALPHA] " + ChatColor.RESET + super.getGuiName();
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
                "Comme le célèbre jeu en ligne, venez",
                "avaler nos succulents Slimes. Il y en a pour",
                "tous les goûts : des petits comme des grands !"
        };
    }

    @Override
    public String[] getDeveloppers()
    {
        return new String[] {
                "Rigner",
                "6infinity8"
        };
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
        return new Location(Hub.getInstance().getHubWorld(), 150.5D, 87.0D, 46.5D);
    }

    @Override
    public boolean isLocked()
    {
        return false;
    }

    @Override
    public List<DisplayedStat> getDisplayedStats()
    {
        return null;
    }
}
