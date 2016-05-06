package net.samagames.hub.cosmetics.pets;

import com.dsh105.echopet.api.EchoPetAPI;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetType;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.hub.cosmetics.common.CosmeticAccessibility;
import net.samagames.hub.cosmetics.common.CosmeticRarity;
import org.bukkit.inventory.ItemStack;

class PetCosmetic extends AbstractCosmetic
{
    private final PetType petType;
    private final PetData[] petDatas;

    PetCosmetic(Hub hub, long storageId, String displayName, ItemStack icon, int stars, CosmeticRarity rarity, CosmeticAccessibility accessibility, String[] description, PetType petType, PetData... petDatas)
    {
        super(hub, storageId, displayName, icon, stars, rarity, accessibility, description);

        this.petType = petType;
        this.petDatas = petDatas;
    }

    public void applyCustomization(IPet pet)
    {
        if (this.petDatas != null)
            for (PetData data : this.petDatas)
                EchoPetAPI.getAPI().addData(pet, data);
    }

    public PetType getPetType()
    {
        return this.petType;
    }
}
