package net.samagames.hub.cosmetics.pets;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.hub.cosmetics.common.CosmeticAccessibility;
import net.samagames.hub.cosmetics.common.CosmeticRarity;
import org.bukkit.inventory.ItemStack;

public abstract class PetCosmetic extends AbstractCosmetic
{
    private final PetType petType;

    public PetCosmetic(Hub hub, String key, String displayName, ItemStack icon, int stars, CosmeticRarity rarity, CosmeticAccessibility accessibility, String[] description, PetType petType)
    {
        super(hub, "pet", key, displayName, icon, stars, rarity, accessibility, description);

        this.petType = petType;
    }

    public abstract void applyCustomization(IPet pet);

    public PetType getPetType()
    {
        return this.petType;
    }

    @Override
    public int compareTo(AbstractCosmetic cosmetic)
    {
        if (!(cosmetic instanceof PetCosmetic))
            return 0;

        if (cosmetic.getKey().equals(this.getKey()))
            return 1;
        else
            return 0;
    }
}
