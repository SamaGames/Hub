package net.samagames.hub.cosmetics.pets.types;

import net.samagames.hub.cosmetics.pets.PetCosmetic;
import net.samagames.hub.cosmetics.pets.nms.CosmeticCow;
import org.bukkit.entity.Cow;
import org.bukkit.inventory.ItemStack;

public class PetCow extends PetCosmetic<Cow>
{
    public PetCow(String databaseName, String displayName, ItemStack icon, String[] description)
    {
        super(databaseName, displayName, icon, description, CosmeticCow.class);
    }

    @Override
    public void applySettings(Cow spawned, String settings) {}
}
