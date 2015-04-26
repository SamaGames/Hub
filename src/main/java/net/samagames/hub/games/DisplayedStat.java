package net.samagames.hub.games;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DisplayedStat
{
    private final String databaseName;
    private final String displayName;
    private final ItemStack icon;

    public DisplayedStat(String databaseName, String displayName, ItemStack icon)
    {
        this.databaseName = databaseName;
        this.displayName = displayName;
        this.icon = icon;

        ItemMeta meta = this.icon.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + displayName);

        this.icon.setItemMeta(meta);
    }

    public DisplayedStat(String databaseName, String displayName, Material icon)
    {
        this(databaseName, displayName, new ItemStack(icon, 1));
    }

    public String getDatabaseName()
    {
        return this.databaseName;
    }

    public String getDisplayName()
    {
        return this.displayName;
    }

    public ItemStack getIcon()
    {
        return this.icon;
    }
}
