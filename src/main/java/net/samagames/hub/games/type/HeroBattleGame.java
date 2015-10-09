package net.samagames.hub.games.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.DisplayedStat;
import net.samagames.hub.games.shop.ShopCategory;
import net.samagames.hub.games.shop.ShopImprovableItem;
import net.samagames.tools.ItemUtils;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class HeroBattleGame extends AbstractGame
{
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
    public ItemStack getIcon()
    {
        return new ItemStack(Material.INK_SACK, 1, DyeColor.RED.getDyeData());
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "Prochainement..."
        };
    }

    @Override
    public int getSlotInMainMenu()
    {
        return 30;
    }

    @Override
    public ShopCategory getShopConfiguration()
    {
    	ShopCategory parentCategory = new ShopCategory(this, null, null, null, -1, null);
    	
    	List<String> classes = Arrays.asList("brute", "guerrier", "archer", "mage", "druide", "pyrobarbare", "cryogenie");
    	List<String> classesDisplays = Arrays.asList("Brute", "Guerrier", "Archer", "Mage", "Druide", "Pyrobarbare", "Cryogénie");
    	List<Material> classesItems = Arrays.asList(Material.DIAMOND_CHESTPLATE, Material.DIAMOND_SWORD, Material.BOW, Material.POTION, Material.GOLDEN_APPLE, Material.ICE, Material.FLINT_AND_STEEL);
        List<Integer> prices = Arrays.asList(0, 0, 0, 0, 3000, 5000, 5000);
    	String cooldownPrefix = ChatColor.GOLD + "Temps réduit de " + ChatColor.AQUA;
    	String powerPrefix = ChatColor.GOLD + "Niveau " + ChatColor.AQUA;
        ShopCategory[] categories = new ShopCategory[classes.size()];
        
        int i = 0;
        for (String className : classes)
        {
        	ShopImprovableItem cooldown = new ShopImprovableItem(this, className + ".cooldown", "Réduction des cooldowns", new ItemStack(Material.WEB), 0, new String[]{"Réduisez le temps de rafraîchissement", "de toutes les capacités pour", "cette classe."});
        	cooldown.addDefault(cooldownPrefix + "0%");
        	cooldown.addLevel(500, cooldownPrefix + "5%" + ChatColor.GOLD + " à " + ChatColor.AQUA + "10%", "1");
        	cooldown.addLevel(1000, cooldownPrefix + "10%" + ChatColor.GOLD + " à " + ChatColor.AQUA + "15%", "2");
        	cooldown.addLevel(5000, cooldownPrefix + "15%" + ChatColor.GOLD + " à " + ChatColor.AQUA + "20%", "3");
        	cooldown.addLevel(20000, cooldownPrefix + "20%" + ChatColor.GOLD + " à " + ChatColor.AQUA + "25%", "4");
        	cooldown.addLevel(50000, cooldownPrefix + "25%" + ChatColor.GOLD + " à " + ChatColor.AQUA + "30%", "5");
        	
        	ShopImprovableItem power = new ShopImprovableItem(this, className + ".power", "Puissance des capacités", new ItemStack(Material.BLAZE_POWDER), 1, new String[]{"Augmentez la puissance et la durée", "de toutes les capacités pour", "cette classe."});
        	power.addDefault(powerPrefix + "0");
        	power.addLevel(750, powerPrefix + "1", "1");
        	power.addLevel(2000, powerPrefix + "2", "2");
        	power.addLevel(10000, powerPrefix + "3", "3");
        	power.addLevel(30000, powerPrefix + "4", "4");
        	power.addLevel(75000, powerPrefix + "5", "5");
        	
        	ShopImprovableItem tools = new ShopImprovableItem(this, className + ".tools", "Nouvelles capacités", new ItemStack(Material.NETHER_STAR), 2, new String[]{"Apprenez une nouvelle capacité", "à chaque niveau d'amélioration", "pour cette classe."});
        	tools.addDefault(powerPrefix + "0");
        	tools.addLevel(20000, powerPrefix + "1", "1");
        	tools.addLevel(50000, powerPrefix + "2", "2");
        	
        	categories[i] = new ShopCategory(this, "class." + className, classesDisplays.get(i), new ItemStack(classesItems.get(i)), i, new String[]{});
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
    public ArrayList<DisplayedStat> getDisplayedStats()
    {
        return null;
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
