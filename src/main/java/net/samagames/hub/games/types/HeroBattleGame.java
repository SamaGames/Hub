package net.samagames.hub.games.types;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.shops.ShopBuyableCategory;
import net.samagames.hub.games.shops.ShopCategory;
import net.samagames.hub.games.shops.ShopImprovableItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class HeroBattleGame extends AbstractGame
{
    public HeroBattleGame(Hub hub)
    {
        super(hub);
    }

    @Override
    public String getCodeName()
    {
        return "herobattle";
    }

    @Override
    public String getName()
    {
        return "HeroBattle";
    }

    @Override
    public String getCategory()
    {
        return "PvP";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.MAGMA_CREAM);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "Tel que sur Smash, choisissez votre",
                "classe puis battez-vous dans des",
                "univers uniques !"
        };
    }

    @Override
    public String[] getDeveloppers()
    {
        return new String[] {
                "AmauryPi",
                "6infinity8"
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
        ShopCategory parentCategory = new ShopCategory(this.hub, this, null, null, null, -1, null);

        List<String> classes = Arrays.asList("brute", "guerrier", "archer", "mage", "mineur", "gardien", "druide", "cryogenie", "pyrobarbare");
        List<String> classesDisplays = Arrays.asList("Brute", "Guerrier", "Archer", "Mage", "Mineur", "Gardien", "Druide", "Cryogénie", "Pyrobarbare");
        List<Material> classesItems = Arrays.asList(Material.DIAMOND_CHESTPLATE, Material.DIAMOND_SWORD, Material.BOW, Material.POTION, Material.DIAMOND_PICKAXE, Material.LONG_GRASS, Material.GOLDEN_APPLE, Material.ICE, Material.FLINT_AND_STEEL);
        List<Integer> prices = Arrays.asList(0, 0, 0, 0, 0, 0, 3000, 5000, 5000);
        String cooldownPrefix = "Temps réduit de ";
        String powerPrefix = "Niveau ";
        ShopCategory[] categories = new ShopCategory[classes.size()];

        int i = 0;
        for (String className : classes)
        {
            ShopImprovableItem cooldown = new ShopImprovableItem(this.hub, this, className + ".cooldown", "Réduction des cooldowns", new ItemStack(Material.WEB), 21, new String[]{"Réduisez le temps de rafraîchissement", "de toutes les capacités pour", "cette classe."});
            cooldown.addDefault(cooldownPrefix + "0%");
            cooldown.addLevel(500, cooldownPrefix + "5% à 10%", "1");
            cooldown.addLevel(1000, cooldownPrefix + "10% à 15%", "2");
            cooldown.addLevel(5000, cooldownPrefix + "15% à 20%", "3");
            cooldown.addLevel(20000, cooldownPrefix + "20% à 25%", "4");
            cooldown.addLevel(50000, cooldownPrefix + "25% à 30%", "5");

            ShopImprovableItem power = new ShopImprovableItem(this.hub, this, className + ".power", "Puissance des capacités", new ItemStack(Material.BLAZE_POWDER), 22, new String[]{"Augmentez la puissance et la durée", "de toutes les capacités pour", "cette classe."});
            power.addDefault(powerPrefix + "0");
            power.addLevel(750, powerPrefix + "1", "1");
            power.addLevel(2000, powerPrefix + "2", "2");
            power.addLevel(10000, powerPrefix + "3", "3");
            power.addLevel(30000, powerPrefix + "4", "4");
            power.addLevel(75000, powerPrefix + "5", "5");

            ShopImprovableItem tools = new ShopImprovableItem(this.hub, this, className + ".tools", "Nouvelles capacités", new ItemStack(Material.NETHER_STAR), 23, new String[]{"Apprenez une nouvelle capacité", "à chaque niveau d'amélioration", "pour cette classe."});
            tools.addDefault(powerPrefix + "0");
            tools.addLevel(20000, powerPrefix + "1", "1");
            tools.addLevel(50000, powerPrefix + "2", "2");

            categories[i] = new ShopBuyableCategory(this.hub, this, "class." + className, classesDisplays.get(i), new ItemStack(classesItems.get(i)), i + 18, new String[]{}, prices.get(i));
            categories[i].addContent(cooldown);
            categories[i].addContent(power);
            categories[i].addContent(tools);
            i++;
        }

        for (ShopCategory category : categories)
            parentCategory.addContent(category);

        return parentCategory;
    }

    @Override
    public Location getLobbySpawn()
    {
        return new Location(this.hub.getWorld(), -53D, 114.0D, 35D, 0.0F, 0.0F);
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
