package net.samagames.hub.cosmetics.disguises;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.hub.cosmetics.common.CosmeticAccessibility;
import net.samagames.hub.cosmetics.common.CosmeticRarity;
import org.bukkit.inventory.ItemStack;

class DisguiseCosmetic extends AbstractCosmetic
{
    private final DisguiseType disguiseType;

    DisguiseCosmetic(Hub hub, String key, String displayName, ItemStack icon, int stars, CosmeticRarity rarity, CosmeticAccessibility accessibility, String[] description, DisguiseType disguiseType)
    {
        super(hub, "disguise", key, displayName, icon, stars, rarity, accessibility, description);

        this.disguiseType = disguiseType;
    }

    DisguiseType getDisguiseType()
    {
        return this.disguiseType;
    }

    @Override
    public int compareTo(AbstractCosmetic cosmetic)
    {
        if (!(cosmetic instanceof DisguiseCosmetic))
            return 0;

        if (cosmetic.getKey().equals(this.getKey()))
            return 1;
        else
            return 0;
    }
}
