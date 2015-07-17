package net.samagames.hub.games.type;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.DisplayedStat;
import net.samagames.hub.games.shop.ShopCategory;
import net.samagames.hub.games.shop.ShopImprovableItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class UpperVoidGame extends AbstractGame
{
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
    public ItemStack getIcon()
    {
        return new ItemStack(Material.STICK, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "Affrontez les autres joueurs dans une arène.",
                "Faites-les tomber dans le vide à l'aide de vos",
                "impitoyables TNT's, mais gare à ne pas tomber à",
                "votre tour !"
        };
    }

    @Override
    public int getSlotInMainMenu()
    {
        return 20;
    }

    @Override
    public ShopCategory getShopConfiguration()
    {
        ShopCategory parentCategory = new ShopCategory(this, null, null, null, -1, null);

        ShopImprovableItem tntStick = new ShopImprovableItem(this, "shooter", "Shooter", new ItemStack(Material.STICK, 1), 13, new String[] {
                "Augmentez la qualité de votre shooter, donc",
                "son temps de rechargement en augmentant cette",
                "amélioration."
        });

        tntStick.addDefault("Basic (2s)");
        tntStick.addLevel(1500, "ChaosGrabber (1.7s)", "chaosgrabber");
        tntStick.addLevel(7000, "BladeSpinner (1.5s)", "bladespinner");

        parentCategory.addContent(tntStick);

        ShopImprovableItem grenadesItem = new ShopImprovableItem(this, "grenade", "Grenades", new ItemStack(Material.CLAY_BALL, 1), 30, new String[] {
                "Augmentez votre nombre de grenades",
                "en jeu en augmentant cette amélioration.",
        });

        grenadesItem.addDefault("1 grenade");
        grenadesItem.addLevel(550, "2 grenades", "grenade-1");
        grenadesItem.addLevel(2100, "3 grenades", "grenade-2");
        grenadesItem.addLevel(5300, "4 grenades", "grenade-3");
        grenadesItem.addLevel(19800, "5 grenades", "grenade-4");
        grenadesItem.addLevel(50000, "6 grenades", "grenade-5");

        parentCategory.addContent(grenadesItem);

        ShopImprovableItem grapnelItem = new ShopImprovableItem(this, "grapins", "Grapin", new ItemStack(Material.LEASH, 1), 32, new String[] {
                "Augmentez le nombre d'utilisation de votre",
                "grapin en augmentant cette amélioration.",
        });

        grapnelItem.addDefault("1 utilisation");
        grapnelItem.addLevel(1500, "2 utilisations", "grapin-1");
        grapnelItem.addLevel(7000, "3 utilisations", "grapin-2");
        grapnelItem.addLevel(24000, "4 utilisations", "grapin-3");
        grapnelItem.addLevel(60000, "5 utilisations", "grapin-4");

        parentCategory.addContent(grapnelItem);

        return parentCategory;
    }

    @Override
    public ArrayList<DisplayedStat> getDisplayedStats()
    {
        ArrayList<DisplayedStat> stats = new ArrayList<>();

        stats.add(new DisplayedStat("wins", "Victoires", Material.NETHER_STAR));
        stats.add(new DisplayedStat("blocs", "Blocs cassés", Material.QUARTZ_BLOCK));
        stats.add(new DisplayedStat("tntlaunch", "TNTs lancées", Material.TNT));
        stats.add(new DisplayedStat("grenade", "Grenades lancées", Material.CLAY_BALL));
        stats.add(new DisplayedStat("played_games", "Parties jouées", Material.SIGN));

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
