package net.samagames.hub.cosmetics.common;

import net.samagames.hub.Hub;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public abstract class AbstractCosmeticRegistry<COSMETIC extends AbstractCosmetic>
{
    protected final Hub hub;
    private final Map<String, COSMETIC> elements;

    public AbstractCosmeticRegistry(Hub hub)
    {
        this.hub = hub;
        this.elements = new HashMap<>();
    }

    public abstract void register();

    protected void registerElement(COSMETIC element)
    {
        if(this.elements.containsKey(element.getKey()))
        {
            this.hub.getCosmeticManager().log(Level.SEVERE, "Cosmetic already registered with the database name '" + element.getKey() + "'!");
            return;
        }

        this.elements.put(element.getKey(), element);
    }

    public COSMETIC getElementByStorageName(String databaseName)
    {
        if(this.elements.containsKey(databaseName))
            return this.elements.get(databaseName);
        else
            return null;
    }

    public Map<String, COSMETIC> getElements()
    {
        return this.elements;
    }
}