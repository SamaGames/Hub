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

    DisguiseCosmetic(Hub hub, int storageId, DisguiseType disguiseType) throws Exception
    {
        super(hub, storageId);

        this.disguiseType = disguiseType;
    }

    public DisguiseType getDisguiseType()
    {
        return this.disguiseType;
    }
}
