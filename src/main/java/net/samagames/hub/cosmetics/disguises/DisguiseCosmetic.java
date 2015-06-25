package net.samagames.hub.cosmetics.disguises;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import org.bukkit.inventory.ItemStack;

public class DisguiseCosmetic extends AbstractCosmetic
{
    private final DisguiseType disguise;

    public DisguiseCosmetic(String databaseName, String displayName, ItemStack icon, String[] description, DisguiseType disguise)
    {
        super("disguise." + databaseName, displayName, icon, description);
        this.disguise = disguise;
    }

    public DisguiseType getDisguiseType()
    {
        return this.disguise;
    }
}
