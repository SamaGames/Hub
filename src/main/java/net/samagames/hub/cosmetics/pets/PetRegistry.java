package net.samagames.hub.cosmetics.pets;

import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import net.samagames.hub.cosmetics.pets.types.PetBlackHorse;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PetRegistry extends AbstractCosmeticRegistry<PetCosmetic>
{
    @Override
    public void register()
    {
        PetBlackHorse blackHorsePet = new PetBlackHorse("blackhorse", "Cheval Noir", new ItemStack(Material.INK_SACK, 1, DyeColor.BLACK.getDyeData()), new String[] {
                "Rapide et fier, il vous menera à",
                "l'aventure !"
        });
        blackHorsePet.buyableWithStars(1700);

        this.registerElement(blackHorsePet);
    }
}
