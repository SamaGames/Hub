package net.samagames.hub.npcs;

import net.samagames.hub.npcs.actions.AbstractNPCAction;
import org.bukkit.Location;
import org.bukkit.entity.Villager;

import java.util.UUID;

public class NPC
{
    private UUID id;
    private String name;
    private Villager.Profession profession;
    private Location location;
    private UUID hologramID;
    private AbstractNPCAction action;

    public NPC(UUID id, String name, Villager.Profession profession, Location location, AbstractNPCAction action)
    {
        this.id = id;
        this.name = name;
        this.profession = profession;
        this.location = location;
        this.action = action;
    }

    public NPC(UUID id, String name, Villager.Profession profession, Location location, String actionClassName)
    {
        this.id = id;
        this.name = name;
        this.profession = profession;
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

    public Villager.Profession getProfession()
    {
        return this.profession;
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
