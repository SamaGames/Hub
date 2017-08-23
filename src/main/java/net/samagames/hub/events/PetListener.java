package net.samagames.hub.events;

import com.dsh105.echopet.compat.api.event.PetInteractEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
public class PetListener implements Listener
{
    @EventHandler
    public void onPetInteract(PetInteractEvent event)
    {
        if (event.getPlayer().getUniqueId().equals(event.getPet().getOwnerUUID()))
            if (event.getAction() == PetInteractEvent.Action.RIGHT_CLICK && !event.getPet().isOwnerRiding())
                event.getPet().ownerRidePet(true);
    }
}
