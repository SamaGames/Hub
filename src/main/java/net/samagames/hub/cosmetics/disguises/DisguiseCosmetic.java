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

    DisguiseCosmetic(Hub hub, long storageId, String displayName, ItemStack icon, int stars, CosmeticRarity rarity, CosmeticAccessibility accessibility, String[] description, DisguiseType disguiseType)
    {
        super(hub, storageId, displayName, icon, stars, rarity, accessibility, description);

        this.disguiseType = disguiseType;
    }

    DisguiseType getDisguiseType()
    {
        return this.disguiseType;
    }
}
