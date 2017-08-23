package net.samagames.hub.cosmetics.disguises;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
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
class DisguiseRegistry extends AbstractCosmeticRegistry<DisguiseCosmetic>
{
    DisguiseRegistry(Hub hub)
    {
        super(hub);
    }

    @Override
    public void register() throws Exception
    {
        DisguiseCosmetic zombieDisguise = new DisguiseCosmetic(this.hub, 19, DisguiseType.ZOMBIE);
        DisguiseCosmetic giantDisguise = new DisguiseCosmetic(this.hub, 20, DisguiseType.GIANT);
        DisguiseCosmetic zombiePigmanDisguise = new DisguiseCosmetic(this.hub, 21, DisguiseType.PIG_ZOMBIE);
        DisguiseCosmetic creeperDisguise = new DisguiseCosmetic(this.hub, 22, DisguiseType.CREEPER);
        DisguiseCosmetic chickenDisguise = new DisguiseCosmetic(this.hub, 23, DisguiseType.CHICKEN);
        DisguiseCosmetic villagerDisguise = new DisguiseCosmetic(this.hub, 24, DisguiseType.VILLAGER);
        DisguiseCosmetic pigDisguise = new DisguiseCosmetic(this.hub, 25, DisguiseType.PIG);
        DisguiseCosmetic ironGolemDisguise = new DisguiseCosmetic(this.hub, 26, DisguiseType.IRON_GOLEM);
        DisguiseCosmetic rabbitDisguise = new DisguiseCosmetic(this.hub, 27, DisguiseType.RABBIT);
        DisguiseCosmetic witchDisguise = new DisguiseCosmetic(this.hub, 28, DisguiseType.WITCH);
        DisguiseCosmetic squidDisguise = new DisguiseCosmetic(this.hub, 29, DisguiseType.SQUID);
        DisguiseCosmetic sheepDisguise = new DisguiseCosmetic(this.hub, 30, DisguiseType.SHEEP);
        DisguiseCosmetic caveSpiderDisguise = new DisguiseCosmetic(this.hub, 31, DisguiseType.CAVE_SPIDER);
        DisguiseCosmetic blazeDisguise = new DisguiseCosmetic(this.hub, 32, DisguiseType.BLAZE);
        DisguiseCosmetic witherSkeletonDisguise = new DisguiseCosmetic(this.hub, 33, DisguiseType.WITHER_SKELETON);
        DisguiseCosmetic batDisguise = new DisguiseCosmetic(this.hub, 34, DisguiseType.BAT);

        this.registerElement(zombieDisguise);
        this.registerElement(giantDisguise);
        this.registerElement(zombiePigmanDisguise);
        this.registerElement(creeperDisguise);
        this.registerElement(chickenDisguise);
        this.registerElement(villagerDisguise);
        this.registerElement(pigDisguise);
        this.registerElement(ironGolemDisguise);
        this.registerElement(rabbitDisguise);
        this.registerElement(witchDisguise);
        this.registerElement(squidDisguise);
        this.registerElement(sheepDisguise);
        this.registerElement(caveSpiderDisguise);
        this.registerElement(blazeDisguise);
        this.registerElement(witherSkeletonDisguise);
        this.registerElement(batDisguise);
    }
}
