package net.samagames.hub.cosmetics.pets.types;

import net.samagames.hub.cosmetics.pets.PetCosmetic;
import net.samagames.hub.cosmetics.pets.nms.CosmeticIronGolem;
import org.bukkit.entity.IronGolem;
import org.bukkit.inventory.ItemStack;

public class PetIronGolem extends PetCosmetic<IronGolem>
{
    public PetIronGolem(String databaseName, String displayName, ItemStack icon, String[] description)
    {
        super(databaseName, displayName, icon, description, CosmeticIronGolem.class);
    }

    @Override
    public void applySettings(IronGolem spawned, String settings) {}
}
