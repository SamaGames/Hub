package net.samagames.hub.gui;

import net.samagames.hub.Hub;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class AbstractGui extends net.samagames.api.gui.AbstractGui
{
    protected final Hub hub;

    public AbstractGui(Hub hub)
    {
        this.hub = hub;
    }

    public ItemStack getBackIcon()
    {
        ItemStack stack = new ItemStack(Material.EMERALD, 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "« Retour");
        stack.setItemMeta(meta);

        return stack;
    }
}
