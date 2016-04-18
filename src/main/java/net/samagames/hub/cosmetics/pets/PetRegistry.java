package net.samagames.hub.cosmetics.pets;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import net.samagames.hub.cosmetics.common.CosmeticAccessibility;
import net.samagames.hub.cosmetics.common.CosmeticRarity;
import net.samagames.hub.cosmetics.pets.types.PetChickenCosmetic;
import net.samagames.hub.utils.EggUtils;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

class PetRegistry extends AbstractCosmeticRegistry<PetCosmetic>
{
    private final List<String> registeredNames;

    PetRegistry(Hub hub)
    {
        super(hub);

        this.registeredNames = new ArrayList<>();
    }

    @Override
    public void register()
    {
        PetCosmetic chickenPet = new PetChickenCosmetic(this.hub, "chicken", "Poulet", EggUtils.getMonsterEgg(EntityType.CHICKEN), 1500, CosmeticRarity.LEGENDARY, CosmeticAccessibility.ALL, new String[] {
                "Même le héros du temps ne peut",
                "pas vous attraper !"
        });

        this.registerElement(chickenPet);
    }
}
