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

public class UHCGame extends AbstractGame
{
    @Override
    public String getCodeName()
    {
        return "uhc";
    }

    @Override
    public String getName()
    {
        return "UHC";
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
                "Créez-vous votre stuff, tuez vos ennemis et",
                "soyez le dernier survivant ! Attention, il n'y",
                "a pas de régénération naturelle des coeurs."
        };
    }

    @Override
    public int getSlotInMainMenu()
    {
        return 21;
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
        return new Location(Hub.getInstance().getHubWorld(), -1.5D, 81.0D, -152.5D, 180.0F, 0.0F);
    }

    @Override
    public boolean isLocked()
    {
        return false;
    }
}
