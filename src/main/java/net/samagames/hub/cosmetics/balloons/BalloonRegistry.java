package net.samagames.hub.cosmetics.balloons;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import org.bukkit.entity.EntityType;

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
class BalloonRegistry extends AbstractCosmeticRegistry<BalloonCosmetic>
{
    BalloonRegistry(Hub hub)
    {
        super(hub);
    }

    @Override
    public void register() throws Exception
    {
        this.registerElement(new BalloonCosmetic(this.hub, 100, EntityType.COW, 1));
        this.registerElement(new BalloonCosmetic(this.hub, 101, EntityType.PIG, 1));
        this.registerElement(new BalloonCosmetic(this.hub, 102, EntityType.SHEEP, 1));
        this.registerElement(new BalloonCosmetic(this.hub, 115, EntityType.SHEEP, "jeb_", 1));
        this.registerElement(new BalloonCosmetic(this.hub, 116, EntityType.SHEEP, "Grumm", 1));
        this.registerElement(new BalloonCosmetic(this.hub, 140, EntityType.MUSHROOM_COW, 1));
    }
}
