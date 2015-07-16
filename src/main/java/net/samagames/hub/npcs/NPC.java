package net.samagames.hub.npcs;

import net.samagames.hub.npcs.actions.AbstractNPCAction;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class NPC
{
    private UUID id;
    private String name;
    private UUID owner;
    private ItemStack[] armor;
    private ItemStack itemInHand;
    private Location location;
    private UUID hologramID;
    private AbstractNPCAction action;

    public NPC(UUID id, String name, UUID owner, ItemStack[] armor, ItemStack itemInHand, Location location, AbstractNPCAction action)
    {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.armor = armor;
        this.itemInHand = itemInHand;
        this.location = location;
        this.action = action;
    }

    public NPC(UUID id, String name, UUID owner, ItemStack[] armor, ItemStack itemInHand, Location location, String actionClassName)
    {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.armor = armor;
        this.itemInHand = itemInHand;
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

    public UUID getOwner()
    {
        return this.owner;
    }

    public ItemStack[] getArmor()
    {
        return this.armor;
    }

    public ItemStack getItemInHand()
    {
        return this.itemInHand;
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
