package net.samagames.hub.cosmetics.common;

import net.samagames.hub.Hub;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

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
        if(this.elements.containsKey(element.getStorageId()))
        {
            this.hub.getCosmeticManager().log(Level.SEVERE, "Cosmetic already registered with the storage id '" + element.getStorageId() + "'!");
            return;
        }

        this.elements.put(element.getStorageId(), element);
    }

    public COSMETIC getElementByStorageId(int storageId)
    {
        if(this.elements.containsKey(storageId))
            return this.elements.get(storageId);
        else
            return null;
    }

    public Map<Integer, COSMETIC> getElements()
    {
        return this.elements;
    }
}