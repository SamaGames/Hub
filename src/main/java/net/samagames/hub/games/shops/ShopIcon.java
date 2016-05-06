package net.samagames.hub.games.shops;

import net.samagames.hub.Hub;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class ShopIcon
{
    protected final Hub hub;
    protected final long storageId;
    protected final ItemStack icon;
    protected final int slot;

    ShopIcon(Hub hub, long storageId, String displayName, ItemStack icon, int slot)
    {
        this.hub = hub;
        this.storageId = storageId;
        this.icon = icon;
        this.slot = slot;

        if(icon != null)
        {
            ItemMeta meta = this.icon.getItemMeta();
            meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GOLD + displayName);
            this.icon.setItemMeta(meta);
        }
    }

    public abstract void execute(Player player, ClickType clickType);
    public abstract ItemStack getFormattedIcon(Player player);

    public long getStorageId()
    {
        return this.storageId;
    }

    public ItemStack getIcon()
    {
        return this.icon;
    }

    public int getSlot()
    {
        return this.slot;
    }
}
