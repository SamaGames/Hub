package net.samagames.hub.cosmetics.gadgets;

import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.hub.cosmetics.gadgets.displayers.AbstractDisplayer;
import org.bukkit.inventory.ItemStack;

public class GadgetCosmetic extends AbstractCosmetic
{
    private final Class<? extends AbstractDisplayer> clazz;
    private final int cooldown;

    public GadgetCosmetic(String databaseName, String displayName, ItemStack icon, String[] description, Class<? extends AbstractDisplayer> clazz, int cooldown)
    {
        super("gadget." + databaseName, displayName, icon, description);

        this.clazz = clazz;
        this.cooldown = cooldown;
    }

    public Class<? extends AbstractDisplayer> getDisplayerClass()
    {
        return this.clazz;
    }

    public int getCooldown()
    {
        return this.cooldown;
    }
}
