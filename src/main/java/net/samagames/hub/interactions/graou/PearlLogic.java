package net.samagames.hub.interactions.graou;

import net.samagames.api.games.pearls.Pearl;
import net.samagames.hub.cosmetics.common.CosmeticRarity;
import net.samagames.tools.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 * <p>
 * Created by Jérémy L. (BlueSlime) on 21/10/2016
 */
class PearlLogic
{
    private enum Star
    {
        ONE(80, 20, 0),
        TWO(65, 30, 5),
        THREE(32, 45, 20),
        FOUR(5, 30, 50),
        FIVE(0, 20, 60)
        ;

        private final int commonPercentage;
        private final int rarePercentage;
        private final int epicPercentage;

        Star(int commonPercentage, int rarePercentage, int epicPercentage)
        {
            this.commonPercentage = commonPercentage;
            this.rarePercentage = rarePercentage;
            this.epicPercentage = epicPercentage;
        }

        /**
         * Return a randomized cosmetic rarity calculated with
         * the percentages.
         *
         * Note: We don't need a legendary percentage because of
         * the total of the percentage have to be equals to 100
         * (obvious).
         *
         * @return A randomized cosmetic rarity
         */
        public CosmeticRarity getRandomizedRarity()
        {
            int random = new Random().nextInt(100);

            if (random <= this.commonPercentage)
                return CosmeticRarity.COMMON;
            else if (random <= this.commonPercentage + this.rarePercentage)
                return CosmeticRarity.RARE;
            else if (random <= this.commonPercentage + this.rarePercentage + this.epicPercentage)
                return CosmeticRarity.EPIC;
            else
                return CosmeticRarity.LEGENDARY;
        }

        public static Star getByCount(int stars)
        {
            if (stars == 1)
                return ONE;
            else if (stars == 2)
                return TWO;
            else if (stars == 3)
                return THREE;
            else if (stars == 4)
                return FOUR;
            else
                return FIVE;
        }
    }

    private static final ItemStack PEARL_HEAD = ItemUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2QxNmFlOTUxMTIwMzk0ZjM2OGYyMjUwYjdjM2FkM2ZiMTJjZWE1NWVjMWIyZGI1YTk0ZDFmYjdmZDRiNmZhIn19fQ==");

    public static ItemStack getIcon(Pearl pearl)
    {
        ItemStack stack = PEARL_HEAD.clone();

        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Perle");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_GRAY + "Niveau " + pearl.getStars());
        lore.add("");
        lore.add(ChatColor.WHITE + "Cette perle si brillante et si");
        lore.add(ChatColor.WHITE + "mystérieuse en fait envier plus");
        lore.add(ChatColor.WHITE + "d'un. " + ChatColor.GOLD + "Graou" + ChatColor.WHITE + " en fait collection");
        lore.add(ChatColor.WHITE + "et vous l'échangera contre un");
        lore.add(ChatColor.WHITE + "cosmétique à la hauteur du niveau");
        lore.add(ChatColor.WHITE + "de cette perle.");
        lore.add("");

        if (pearl.getStars() > 3)
        {
            lore.add(ChatColor.DARK_GRAY + "Nécéssite : " + (pearl.getStars() == 5 ? ChatColor.AQUA + "VIP" + ChatColor.LIGHT_PURPLE + "+" : ChatColor.GREEN + "VIP"));
        }

        lore.add(ChatColor.DARK_GRAY + "Expire dans : " + ChatColor.WHITE + pearl.getExpirationInDays() + " jours");

        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "\u25B6 Cliquez pour échanger");

        meta.setLore(lore);
        stack.setItemMeta(meta);

        return stack;
    }
}
