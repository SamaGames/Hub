package net.samagames.hub.cosmetics.common;

import net.samagames.hub.Hub;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

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
public abstract class AbstractCosmeticRegistry<COSMETIC extends AbstractCosmetic>
{
    protected final Hub hub;
    private final Map<Integer, COSMETIC> elements;

    public AbstractCosmeticRegistry(Hub hub)
    {
        this.hub = hub;
        this.elements = new HashMap<>();
    }

    public abstract void register() throws Exception;

    protected void registerElement(COSMETIC element)
    {
        if (this.elements.containsKey(element.getStorageId()))
        {
            this.hub.getCosmeticManager().log(Level.SEVERE, "Cosmetic already registered with the storage id '" + element.getStorageId() + "'!");
            return;
        }

        this.elements.put(element.getStorageId(), element);
    }

    public COSMETIC getElementByStorageId(int storageId)
    {
        if (this.elements.containsKey(storageId))
            return this.elements.get(storageId);
        else
            return null;
    }

    public Map<Integer, COSMETIC> getElements()
    {
        return this.elements;
    }
}