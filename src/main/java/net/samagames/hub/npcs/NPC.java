package net.samagames.hub.npcs;

import net.samagames.hub.npcs.actions.AbstractNPCAction;
import org.bukkit.Location;

import java.util.UUID;

public class NPC
{
    private UUID id;
    private String name;
    private NPCProperties properties;
    private Location location;
    private UUID hologramID;
    private AbstractNPCAction action;

    public NPC(UUID id, String name, NPCProperties properties, Location location, AbstractNPCAction action)
    {
        this.id = id;
        this.name = name;
        this.properties = properties;
        this.location = location;
        this.action = action;
    }

    public NPC(UUID id, String name, NPCProperties properties, Location location, String actionClassName)
    {
        this.id = id;
        this.name = name;
        this.properties = properties;
        this.location = location;

        AbstractNPCAction action = null;

        try
        {
            Class actionClass = Class.forName(AbstractNPCAction.class.getPackage().getName() + "." + actionClassName);
            action = (AbstractNPCAction) actionClass.newInstance();
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        this.action = action;
    }

    public void setHologramID(UUID hologramID)
    {
        this.hologramID = hologramID;
    }

    public UUID getID()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public NPCProperties getProperties()
    {
        return this.properties;
    }

    public Location getLocation()
    {
        return this.location;
    }

    public UUID getHologramID()
    {
        return this.hologramID;
    }

    public AbstractNPCAction getAction()
    {
        return this.action;
    }
}
