package net.samagames.hub.cosmetics.disguises;

import me.libraryaddict.disguise.disguisetypes.Disguise;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import org.bukkit.inventory.ItemStack;

public class DisguiseCosmetic extends AbstractCosmetic
{
    private final Disguise disguise;

    public DisguiseCosmetic(String databaseName, String displayName, ItemStack icon, String[] description, Disguise disguise)
    {
        super("particle." + databaseName, displayName, icon, description);
        this.disguise = disguise;
    }

    public Disguise getDisguise()
    {
        return this.disguise;
    }
}
