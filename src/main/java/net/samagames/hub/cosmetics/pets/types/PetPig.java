package net.samagames.hub.cosmetics.pets.types;

import net.samagames.hub.cosmetics.pets.PetCosmetic;
import net.samagames.hub.cosmetics.pets.nms.CosmeticPig;
import org.bukkit.entity.Pig;
import org.bukkit.inventory.ItemStack;

public class PetPig extends PetCosmetic<Pig>
{
    public PetPig(String databaseName, String displayName, ItemStack icon, String[] description)
    {
        super(databaseName, displayName, icon, description, CosmeticPig.class);
    }

    @Override
    public void applySettings(Pig spawned, String settings) {}
}
