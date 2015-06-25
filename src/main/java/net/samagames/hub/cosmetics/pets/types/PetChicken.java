package net.samagames.hub.cosmetics.pets.types;

import net.samagames.hub.cosmetics.pets.PetCosmetic;
import net.samagames.hub.cosmetics.pets.nms.CosmeticChicken;
import org.bukkit.entity.Chicken;
import org.bukkit.inventory.ItemStack;

public class PetChicken extends PetCosmetic<Chicken>
{
    public PetChicken(String databaseName, String displayName, ItemStack icon, String[] description)
    {
        super(databaseName, displayName, icon, description, CosmeticChicken.class);
    }

    @Override
    public void applySettings(Chicken spawned, String settings) {}
}
