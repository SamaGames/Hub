package net.samagames.hub.cosmetics.pets.types;

import net.samagames.hub.cosmetics.pets.PetCosmetic;
import net.samagames.hub.cosmetics.pets.nms.CosmeticEnderman;
import org.bukkit.entity.Enderman;
import org.bukkit.inventory.ItemStack;

public class PetEnderman extends PetCosmetic<Enderman>
{
    public PetEnderman(String databaseName, String displayName, ItemStack icon, String[] description)
    {
        super(databaseName, displayName, icon, description, CosmeticEnderman.class);
    }

    @Override
    public void applySettings(Enderman spawned, String settings) {}
}
