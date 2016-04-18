package net.samagames.hub.cosmetics.pets.types;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.CosmeticAccessibility;
import net.samagames.hub.cosmetics.common.CosmeticRarity;
import net.samagames.hub.cosmetics.pets.PetCosmetic;
import org.bukkit.inventory.ItemStack;

public class PetChickenCosmetic extends PetCosmetic
{
    public PetChickenCosmetic(Hub hub, String key, String displayName, ItemStack icon, int stars, CosmeticRarity rarity, CosmeticAccessibility accessibility, String[] description)
    {
        super(hub, key, displayName, icon, stars, rarity, accessibility, description, PetType.CHICKEN);
    }

    @Override
    public void applyCustomization(IPet pet) { /** Not needed **/ }
}
