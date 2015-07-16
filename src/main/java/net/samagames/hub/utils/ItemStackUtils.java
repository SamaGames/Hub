package net.samagames.hub.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemStackUtils
{
    public static String isToStr(ItemStack stack)
    {
        return stack.getType() + ", " + stack.getAmount() + ", " + stack.getDurability();
    }

    public static ItemStack strToIs(String string)
    {
        String[] data = string.split(", ");
        return new ItemStack(Material.valueOf(data[0]), Integer.parseInt(data[1]), Short.parseShort(data[2]));
    }
}
