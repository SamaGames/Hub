package net.samagames.hub.cosmetics.pets.types;

import net.samagames.hub.cosmetics.pets.PetCosmetic;
import net.samagames.hub.cosmetics.pets.nms.CosmeticHorse;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PetSkeletonHorse extends PetCosmetic<Horse>
{
    public PetSkeletonHorse(String databaseName, String displayName, ItemStack icon, String[] description)
    {
        super(databaseName, displayName, icon, description, CosmeticHorse.class);
    }

    @Override
    public Horse spawn(Player player)
    {
        Horse spawned = super.spawn(player);

        spawned.setVariant(Horse.Variant.SKELETON_HORSE);
        spawned.setJumpStrength(1D);

        return spawned;
    }

    @Override
    public void applySettings(Horse spawned, String settings) {}
}
