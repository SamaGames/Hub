package net.samagames.hub.cosmetics.pets;

import com.dsh105.echopet.api.EchoPetAPI;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetType;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;

/*
 * This file is part of Hub.
 *
 * Hub is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hub is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hub.  If not, see <http://www.gnu.org/licenses/>.
 */
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
