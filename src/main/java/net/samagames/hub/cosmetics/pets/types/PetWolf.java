package net.samagames.hub.cosmetics.pets.types;

import net.samagames.hub.cosmetics.pets.PetCosmetic;
import net.samagames.hub.cosmetics.pets.nms.CosmeticWolf;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;

public class PetWolf extends PetCosmetic<Wolf>
{
    public PetWolf(String databaseName, String displayName, ItemStack icon, String[] description)
    {
        super(databaseName, displayName, icon, description, CosmeticWolf.class);
    }

    @Override
    public void applySettings(Wolf spawned, String settings) {}
}
