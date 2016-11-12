package net.samagames.hub.cosmetics.pets;

import com.dsh105.echopet.api.EchoPetAPI;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetType;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;

class PetCosmetic extends AbstractCosmetic
{
    private final PetType petType;
    private final PetData[] petDatas;

    PetCosmetic(Hub hub, int storageId, PetType petType, PetData... petDatas) throws Exception
    {
        super(hub, "Animal de compagnie", storageId);

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
