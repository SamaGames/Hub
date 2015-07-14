package net.samagames.hub.npcs;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class NPCProperties
{
    private final UUID skin;
    private final ItemStack[] armor;
    private final ItemStack itemInHand;

    public NPCProperties(UUID skin, ItemStack[] armor, ItemStack itemInHand)
    {
        this.skin = skin;
        this.armor = armor;
        this.itemInHand = itemInHand;
    }

    public UUID getSkin()
    {
        return this.skin;
    }

    public ItemStack[] getArmor()
    {
        return this.armor;
    }

    public ItemStack getItemInHand()
    {
        return this.itemInHand;
    }
}
