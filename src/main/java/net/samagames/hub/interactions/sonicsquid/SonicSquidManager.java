package net.samagames.hub.interactions.sonicsquid;

import com.google.gson.JsonArray;
import net.minecraft.server.v1_12_R1.EntitySquid;
import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteractionManager;

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
public class SonicSquidManager extends AbstractInteractionManager<SonicSquid>
{
    public SonicSquidManager(Hub hub)
    {
        super(hub, "squid");

        this.hub.getEntityManager().registerEntity("SonicSquid", 94, EntitySquid.class, EntitySonicSquid.class);
    }

    @Override
    public void loadConfiguration(JsonArray rootJson)
    {
        this.interactions.add(new SonicSquid(this.hub));
    }
}
