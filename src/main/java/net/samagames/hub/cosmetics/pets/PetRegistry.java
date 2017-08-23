package net.samagames.hub.cosmetics.pets;

import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetType;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;

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
class PetRegistry extends AbstractCosmeticRegistry<PetCosmetic>
{
    PetRegistry(Hub hub)
    {
        super(hub);
    }

    @Override
    public void register() throws Exception
    {
        PetCosmetic skeletonHorsePet = new PetCosmetic(this.hub, 43, PetType.HORSE, PetData.SKELETON);
        PetCosmetic magmaCubePet = new PetCosmetic(this.hub, 44, PetType.MAGMACUBE, PetData.MEDIUM);
        PetCosmetic slimePet = new PetCosmetic(this.hub, 45, PetType.SLIME, PetData.MEDIUM);
        PetCosmetic zombieHorsePet = new PetCosmetic(this.hub, 46, PetType.HORSE, PetData.ZOMBIE);
        PetCosmetic chickenPet = new PetCosmetic(this.hub, 47, PetType.CHICKEN);
        PetCosmetic wolfPet = new PetCosmetic(this.hub, 48, PetType.WOLF);
        PetCosmetic brownHorsePet = new PetCosmetic(this.hub, 49, PetType.HORSE, PetData.BROWN);
        PetCosmetic cowPet = new PetCosmetic(this.hub, 50, PetType.COW);
        PetCosmetic pigPet = new PetCosmetic(this.hub, 51, PetType.PIG);
        PetCosmetic whiteHorsePet = new PetCosmetic(this.hub, 52, PetType.HORSE, PetData.WHITE);
        PetCosmetic rabbitPet = new PetCosmetic(this.hub, 53, PetType.RABBIT);
        PetCosmetic ironGolemPet = new PetCosmetic(this.hub, 54, PetType.IRONGOLEM);
        PetCosmetic endermanPet = new PetCosmetic(this.hub, 55, PetType.ENDERMAN);
        PetCosmetic whiteSheepPet = new PetCosmetic(this.hub, 56, PetType.SHEEP, PetData.WHITE);
        PetCosmetic orangeSheepPet = new PetCosmetic(this.hub, 57, PetType.SHEEP, PetData.ORANGE);
        PetCosmetic purpleSheepPet = new PetCosmetic(this.hub, 58, PetType.SHEEP, PetData.PURPLE);
        PetCosmetic pinkSheepPet = new PetCosmetic(this.hub, 59, PetType.SHEEP, PetData.PINK);
        PetCosmetic redSheepPet = new PetCosmetic(this.hub, 60, PetType.SHEEP, PetData.RED);
        PetCosmetic blackSheepPet = new PetCosmetic(this.hub, 61, PetType.SHEEP, PetData.BLACK);
        PetCosmetic yellowSheepPet = new PetCosmetic(this.hub, 62, PetType.SHEEP, PetData.YELLOW);
        PetCosmetic mushroomCowPet = new PetCosmetic(this.hub, 63, PetType.MUSHROOMCOW);
        PetCosmetic donkeyPet = new PetCosmetic(this.hub, 64, PetType.HORSE, PetData.DONKEY);
        PetCosmetic slavePet = new PetCosmetic(this.hub, 65, PetType.VILLAGER);

        this.registerElement(skeletonHorsePet);
        this.registerElement(magmaCubePet);
        this.registerElement(slimePet);
        this.registerElement(zombieHorsePet);
        this.registerElement(chickenPet);
        this.registerElement(wolfPet);
        this.registerElement(brownHorsePet);
        this.registerElement(cowPet);
        this.registerElement(pigPet);
        this.registerElement(whiteHorsePet);
        this.registerElement(rabbitPet);
        this.registerElement(ironGolemPet);
        this.registerElement(endermanPet);
        this.registerElement(whiteSheepPet);
        this.registerElement(orangeSheepPet);
        this.registerElement(purpleSheepPet);
        this.registerElement(pinkSheepPet);
        this.registerElement(redSheepPet);
        this.registerElement(blackSheepPet);
        this.registerElement(yellowSheepPet);
        this.registerElement(mushroomCowPet);
        this.registerElement(donkeyPet);
        this.registerElement(slavePet);
    }
}
