package net.samagames.hub.utils;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackUtils
{
    public static ItemStack hideAllAttributes(ItemStack itemStack)
    {
        ItemMeta meta = itemStack.getItemMeta();

        for (ItemFlag itemFlag : ItemFlag.values())
            if (itemFlag.name().startsWith("HIDE_"))
                meta.addItemFlags(itemFlag);

        itemStack.setItemMeta(meta);

        return itemStack;
    }
}
