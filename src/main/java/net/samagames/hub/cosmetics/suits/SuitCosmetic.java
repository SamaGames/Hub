package net.samagames.hub.cosmetics.suits;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.hub.cosmetics.common.CosmeticAccessibility;
import net.samagames.hub.cosmetics.common.CosmeticRarity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

class SuitCosmetic extends AbstractCosmetic {

    private static final String[] displayNames = {"Tête", "Plastron", "Jambes", "Bottes"};
    private static final String[] keys = {"head", "chestplate", "leggings", "boots"};
    private SuitElement[] suit;
    private String suitKey;

    SuitCosmetic(Hub hub, String key, String displayName, CosmeticAccessibility accessibility, String[] description, SuitElement[] suit)
    {
        super(hub, "suit", key, "Ensemble " + displayName, suit[0].getItemStack().clone(), 0, CosmeticRarity.COMMON, accessibility, description);
        this.suit = suit;
        this.suitKey = displayName.toLowerCase();

        for (int i = 0; i < 4; i++)
        {
            ItemMeta meta = this.suit[i].getItemStack().getItemMeta();
            meta.setDisplayName(this.suit[i].getCosmeticRarity().getColor() + displayNames[i] + " " + displayName);
        }
    }

    @Override
    public ItemStack getIcon(Player player) {
        return this.getIcon();
    }

    public int getOwnedParts(Player player) {
        int n = 0;
        List<String> owned = SamaGamesAPI.get().getShopsManager().getOwnedLevels(player.getUniqueId(), "suit");
        for (int i = 0; i < 4; i++)
            if (owned.contains(this.suitKey + "." + keys[i]))
                n++;
        return n;
    }

    static class SuitElement
    {
        private ItemStack itemStack;
        private int stars;
        private CosmeticRarity cosmeticRarity;

        SuitElement(int stars, CosmeticRarity cosmeticRarity, ItemStack itemStack)
        {
            this.stars = stars;
            this.cosmeticRarity = cosmeticRarity;
            this.itemStack = itemStack;
        }

        CosmeticRarity getCosmeticRarity() {
            return cosmeticRarity;
        }

        int getStars() {
            return stars;
        }

        ItemStack getItemStack() {
            return itemStack;
        }
    }
}